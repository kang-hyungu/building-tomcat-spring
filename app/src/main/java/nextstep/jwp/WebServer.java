package nextstep.jwp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebServer {

    private static final Logger log = LoggerFactory.getLogger(WebServer.class);

    private final int port;
    private final ExecutorService pool;

    public WebServer(int port, int poolSize) {
        this.port = checkPort(port);
        this.pool = Executors.newFixedThreadPool(poolSize);
    }

    private int checkPort(int port) {
        if (port <= 0) {
            throw new IllegalArgumentException("port는 0보다 커야 합니다. 입력값: " + port);
        }
        return port;
    }

    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            log.info("Web Server started {} port.", port);

            Socket connection;
            while ((connection = serverSocket.accept()) != null) {
                pool.execute(new HttpRequestHandler(connection));
            }
        } catch (IOException e) {
            log.error("Web Server Exception : {}", e.getMessage());
            pool.shutdown();
        }
    }
}
