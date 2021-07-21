package nextstep.jwp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

public class WebServer {

    private static final Logger logger = LoggerFactory.getLogger(WebServer.class);
    private static final int DEFAULT_PORT = 8080;
    private static final int DEFAULT_POOL_SIZE = 100;

    private final int port;
    private final ExecutorService pool;

    public WebServer(int port) {
        this(port, DEFAULT_POOL_SIZE);
    }

    public WebServer(int port, int poolSize) {
        this.port = checkPort(port);
        this.pool = Executors.newFixedThreadPool(defaultPoolSize(poolSize));
    }

    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("Web Server started {} port.", serverSocket.getLocalPort());
            handle(serverSocket);
        } catch (IOException exception) {
            logger.error("Exception accepting connection", exception);
        } catch (RuntimeException exception) {
            logger.error("Unexpected error", exception);
        } finally {
            pool.shutdown();
        }
    }

    private void handle(ServerSocket serverSocket) throws IOException {
        Socket connection;
        while ((connection = serverSocket.accept()) != null) {
            pool.submit(new RequestHandler(connection));
        }
    }

    public static Integer defaultPortIfNull(String[] args) {
        return Stream.of(args)
                .findFirst()
                .map(Integer::parseInt)
                .orElse(WebServer.DEFAULT_PORT);
    }

    private int checkPort(int port) {
        if (port < 1 || 65535 < port) {
            return DEFAULT_PORT;
        }
        return port;
    }

    private int defaultPoolSize(int poolSize) {
        return Math.max(poolSize, DEFAULT_POOL_SIZE);
    }
}
