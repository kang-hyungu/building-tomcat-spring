package nextstep.jwp;

import nextstep.jwp.http.Constants;
import nextstep.jwp.http.HttpRequest;
import nextstep.jwp.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Objects;

public class RequestHandler implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connection) {
        this.connection = Objects.requireNonNull(connection);
    }

    @Override
    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (final BufferedReader reader = createReader();
             final OutputStream outputStream = createOutputStream()) {

            // mission start
            final String request = read(reader);

            final HttpRequest httpRequest = new HttpRequest(request);
            final byte[] responseBody = loadResource(httpRequest);
            final HttpResponse httpResponse = new HttpResponse(httpRequest.isCss(), responseBody);

            write(outputStream, httpResponse.toBytes());
        } catch (IOException exception) {
            logger.error("Exception stream", exception);
        } catch (URISyntaxException exception) {
            logger.error("Exception file uri syntax", exception);
        } finally {
            close();
        }
    }

    private BufferedReader createReader() throws IOException {
        return new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
    }

    private BufferedOutputStream createOutputStream() throws IOException {
        return new BufferedOutputStream(connection.getOutputStream());
    }

    private String read(BufferedReader reader) throws IOException {
        StringBuilder content = new StringBuilder();
        while (reader.ready()) {
            final String readLine = reader.readLine();
            content.append(readLine)
                    .append(Constants.NEW_LINE);
        }
        return content.toString();
    }

    private void write(OutputStream outputStream, byte[] response) throws IOException {
        outputStream.write(response);
        outputStream.flush();
    }

    private void close() {
        try {
            connection.close();
        } catch (IOException exception) {
            logger.error("Exception closing socket", exception);
        }
    }

    private byte[] loadResource(HttpRequest httpRequest) throws URISyntaxException, IOException {
        final String path = httpRequest.isCss() ? "static/css/signin.css" : "static/sign-in.html";
        final File file = createFile(path);
        return Files.readAllBytes(file.toPath());
    }

    private File createFile(String path) throws URISyntaxException {
        final URL url = getClass().getClassLoader().getResource(path);
        Objects.requireNonNull(url);
        return new File(url.toURI());
    }
}
