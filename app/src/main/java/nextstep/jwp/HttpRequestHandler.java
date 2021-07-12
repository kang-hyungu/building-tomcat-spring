package nextstep.jwp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Objects;

public class HttpRequestHandler implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(HttpRequestHandler.class);

    private final Socket connection;

    public HttpRequestHandler(Socket connection) {
        Objects.requireNonNull(connection);
        this.connection = connection;
    }

    @Override
    public void run() {
        try (InputStream inputStream = connection.getInputStream();
             OutputStream outputStream = connection.getOutputStream()) {

        } catch (IOException e) {
            log.error("HttpRequestHandler Exception : {}", e.getMessage());
        }
    }
}
