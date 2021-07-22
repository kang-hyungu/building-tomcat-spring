package nextstep.jwp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
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

        try (final InputStream inputStream = connection.getInputStream();
             final OutputStream outputStream = connection.getOutputStream()) {

            final String response = String.join("\r\n",
                    "HTTP/1.1 200 OK",
                    "Content-Type: text/html;charset=utf-8",
                    "Content-Length: 11",
                    "",
                    "Hello World");

            outputStream.write(response.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
        } catch (IOException exception) {
            logger.error("Exception stream", exception);
        } finally {
            close();
        }
    }

    private void close() {
        try {
            connection.close();
        } catch (IOException exception) {
            logger.error("Exception closing socket", exception);
        }
    }
}
