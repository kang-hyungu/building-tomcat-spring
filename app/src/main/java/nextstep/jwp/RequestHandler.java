package nextstep.jwp;

import nextstep.jwp.http.HttpRequest;
import nextstep.jwp.http.HttpResponse;
import nextstep.jwp.http.HttpSessions;
import nextstep.jwp.mvc.Controller;
import nextstep.jwp.mvc.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.UUID;

public class RequestHandler implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private final RequestMapping requestMapping;

    public RequestHandler(Socket connection, RequestMapping requestMapping) {
        this.connection = Objects.requireNonNull(connection);
        this.requestMapping = Objects.requireNonNull(requestMapping);
    }

    @Override
    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (final BufferedReader reader = createReader();
             final OutputStream outputStream = createOutputStream()) {

            if (!reader.ready()) {
                return;
            }

            final HttpRequest httpRequest = new HttpRequest(reader);
            final HttpResponse httpResponse = new HttpResponse(outputStream);

            if (httpRequest.getCookies().getCookie(HttpSessions.SESSION_ID_NAME) == null) {
                httpResponse.addHeader("Set-Cookie", HttpSessions.SESSION_ID_NAME + "=" + UUID.randomUUID());
            }

            final Controller controller = requestMapping.getController(httpRequest);
            controller.service(httpRequest, httpResponse);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
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

    private void close() {
        try {
            connection.close();
        } catch (IOException exception) {
            log.error("Exception closing socket", exception);
        }
    }
}
