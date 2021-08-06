package nextstep.jwp;

import nextstep.jwp.db.InMemoryUserRepository;
import nextstep.jwp.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RequestHandler implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connection) {
        this.connection = Objects.requireNonNull(connection);
    }

    @Override
    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (final BufferedReader reader = createReader();
             final OutputStream outputStream = createOutputStream()) {

            String line = reader.readLine();
            if (line == null) {
                return;
            }

            final String method = line.split(" ")[0];
            final String uri = line.split(" ")[1];

            final Map<String, String> headers = new ConcurrentHashMap<>();
            while (!"".equals(line)) {
                line = reader.readLine();
                final String[] header = line.split(":");
                if (header.length == 2) {
                    headers.put(header[0].trim(), header[1].trim());
                }
            }

            byte[] responseBody;
            User user = null;

            if ("/index.html".equals(uri)) {
                responseBody = getResponseBody("index.html");
            } else if("/login".equals(uri)) {
                if ("POST".equals(method)) {
                    final Map<String, String> requestBody = parseRequestBody(reader, headers);
                    user = InMemoryUserRepository.findByAccount(requestBody.get("account"))
                            .orElseThrow(RuntimeException::new);
                    log.info("User : {}", user);
                }

                responseBody = getResponseBody("login.html");
            } else if ("/register".equals(uri)) {
                if ("POST".equals(method)) {
                    final Map<String, String> requestBody = parseRequestBody(reader, headers);
                    user = new User(2, requestBody.get("account"), requestBody.get("password"), requestBody.get("email"));
                    InMemoryUserRepository.save(user);
                }

                responseBody = getResponseBody("register.html");
            } else if (uri.endsWith(".css") || uri.endsWith(".js")) {
                responseBody = getResponseBody(uri);
            } else {
                responseBody = "Hello World".getBytes();
            }

            if (uri.endsWith(".css")) {
                outputStream.write(getResourceHeader("text/css;charset=utf-8;", responseBody).getBytes());
            } else if (uri.endsWith(".js")) {
                outputStream.write(getResourceHeader("application/javascript;", responseBody).getBytes());
            } else {
                outputStream.write(getResponseHeader(responseBody, user));
            }
            outputStream.write(responseBody);
            outputStream.flush();
        } catch (IOException exception) {
            log.error("Exception stream", exception);
        } finally {
            close();
        }
    }

    private Map<String, String> parseRequestBody(BufferedReader reader, Map<String, String> headers) throws IOException {
        int contentLength = Integer.parseInt(headers.get("Content-Length"));
        char[] buffer = new char[contentLength];
        reader.read(buffer, 0, contentLength);
        return parseQueryString(new String(buffer));
    }

    private byte[] getResponseBody(String fileName) throws IOException {
        final URL resource = getClass().getClassLoader().getResource("static/" + fileName);
        return Files.readAllBytes(new File(resource.getPath()).toPath());
    }

    private Map<String, String> parseQueryString(String queryString) {
        return Stream.of(queryString.split("&"))
                .map(param -> param.split("="))
                .collect(Collectors.toMap(param -> param[0], param -> param[1]));
    }

    private byte[] getResponseHeader(byte[] responseBody, User user) {
        if (user == null) {
            return getResponseHeader(responseBody).getBytes();
        } else {
            return redirect("index.html").getBytes();
        }
    }

    private String getResponseHeader(byte[] responseBody) {
        return String.join("\r\n",
                "HTTP/1.1 200 OK ",
                "Content-Type: text/html;charset=utf-8 ",
                "Content-Length: " + responseBody.length + " ",
                "",
                "");
    }

    private String getResourceHeader(String contentType, byte[] responseBody) {
        return String.join("\r\n",
                "HTTP/1.1 200 OK ",
                "Content-Type: " + contentType + " ",
                "Content-Length: " + responseBody.length + " ",
                "",
                "");
    }

    private String redirect(String location) {
        return String.join("\r\n",
                "HTTP/1.1 302 Found ",
                "Location: " + location + " ",
                "",
                "");
    }

    private BufferedReader createReader() throws IOException {
        return new BufferedReader(new InputStreamReader(connection.getInputStream()));
    }

    private BufferedOutputStream createOutputStream() throws IOException {
        return new BufferedOutputStream(connection.getOutputStream());
    }

    private void close() {
        try {
            connection.close();
        } catch (IOException exception) {
            log.error("Exception closing socket", exception);
        }
    }
}
