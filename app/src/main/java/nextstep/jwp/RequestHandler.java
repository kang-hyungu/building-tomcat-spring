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

        try (final BufferedReader reader = createReader();
             final Writer writer = createWriter()) {

            // mission start
            final String request = readRequest(reader);
            writeResponse(writer, "Hello World");

        } catch (IOException exception) {
            logger.error("Exception stream", exception);
        } finally {
            close();
        }
    }

    private BufferedReader createReader() throws IOException {
        return new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
    }

    private BufferedWriter createWriter() throws IOException {
        return new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
    }

    private String readRequest(BufferedReader reader) throws IOException {
        StringBuilder content = new StringBuilder();
        while (reader.ready()) {
            final String readLine = reader.readLine();
            content.append(readLine);
            content.append("\r\n");
        }
        return content.toString();
    }

    private void writeResponse(Writer writer, String responseBody) throws IOException {
        writer.write("HTTP/1.1 200 OK\r\n");
        writer.write("Content-Type: text/html;charset=utf-8\r\n");
        writer.write("Content-Length: " + responseBody.getBytes(StandardCharsets.UTF_8).length + "\r\n");
        writer.write("\r\n");
        writer.write(responseBody);
        writer.flush();
    }

    private void close() {
        try {
            connection.close();
        } catch (IOException exception) {
            logger.error("Exception closing socket", exception);
        }
    }
}
