package nextstep.jwp;

import com.google.common.primitives.Ints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebServer {

    private static final Logger logger = LoggerFactory.getLogger(WebServer.class);

    private final int port;
    private final ExecutorService pool;

    public WebServer(int port, int poolSize) {
        this.port = checkPort(port);
        this.pool = Executors.newFixedThreadPool(defaultPoolSize(poolSize));
    }

    private int checkPort(int port) {
        return Ints.constrainToRange(port, 1, 65535);
    }

    private int defaultPoolSize(int poolSize) {
        final int DEFAULT_POOL_SIZE = 100;
        return Math.max(poolSize, DEFAULT_POOL_SIZE);
    }

    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("Web Server started {} port.", serverSocket.getLocalPort());

            Socket connection;
            while ((connection = serverSocket.accept()) != null) {
                pool.submit(new HttpRequestHandler(connection));
            }
        } catch (IOException exception) {
            logger.error("Exception accepting connection", exception);
        } catch (RuntimeException exception) {
            logger.error("Unexpected error", exception);
        } finally {
            pool.shutdown();
        }
    }
}
