package nextstep.jwp.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;

public class HttpResponse {

    private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);

    private final DataOutputStream outputStream;
    private final HttpHeaders headers;

    public HttpResponse(OutputStream outputStream) {
        this.outputStream = new DataOutputStream(outputStream);
        this.headers = new HttpHeaders();
    }

    public <T> void addHeader(String key, T value) {
        headers.put(key, String.valueOf(value));
    }

    public void forward(String path) {
        final byte[] body = read(path);
        addHeader("Content-Type", "text/html;charset=utf-8;");
        addHeader("Content-Length", body.length);
        responseOk();
        responseBody(body);
    }

    public byte[] read(String fileName) {
        try {
            final URL resource = getClass().getClassLoader().getResource("static/" + fileName);
            return Files.readAllBytes(new File(resource.getPath()).toPath());
        } catch (IOException | NullPointerException e) {
            log.error("file name: {}, message: {}", fileName, e.getMessage());
            return new byte[0];
        }
    }

    public void responseOk() {
        try {
            outputStream.writeBytes(StatusLine.OK.withNewLine());
            outputStream.writeBytes(headers.value());
            outputStream.writeBytes(HttpConstants.NEW_LINE);
            outputStream.writeBytes(HttpConstants.NEW_LINE);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void sendRedirect(String location) {
        try {
            outputStream.writeBytes(StatusLine.FOUND.withNewLine());
            outputStream.writeBytes(headers.value());
            outputStream.writeBytes("Location: " + location);
            outputStream.writeBytes(HttpConstants.NEW_LINE);
            outputStream.writeBytes(HttpConstants.NEW_LINE);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void responseNotFound() {
        try {
            outputStream.writeBytes(StatusLine. NOT_FOUND.withNewLine());
            outputStream.writeBytes(headers.value());
            outputStream.writeBytes(HttpConstants.NEW_LINE);
            outputStream.writeBytes(HttpConstants.NEW_LINE);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void responseBody(byte[] body) {
        try {
            outputStream.write(body, 0, body.length);
            outputStream.writeBytes(HttpConstants.NEW_LINE);
            outputStream.flush();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
