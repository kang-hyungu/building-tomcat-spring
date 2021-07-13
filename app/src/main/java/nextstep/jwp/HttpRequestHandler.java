package nextstep.jwp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.util.Objects;

public class HttpRequestHandler implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(HttpRequestHandler.class);

    private final Socket connection;

    public HttpRequestHandler(Socket connection) {
        this.connection = Objects.requireNonNull(connection);
    }

    @Override
    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (InputStream inputStream = new BufferedInputStream(connection.getInputStream());
             OutputStream outputStream = new BufferedOutputStream(connection.getOutputStream())) {

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
