package nextstep.jwp.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HttpRequest {

    private final HttpMethod method;
    private final String uri;
    private final Map<String, String> headers = new ConcurrentHashMap<>();
    private final Map<String, String> requestBody;

    public HttpRequest(BufferedReader reader) throws IOException {
        String line = reader.readLine();
        Objects.requireNonNull(line);

        this.method = HttpMethod.of(line.split(" ")[0]);
        this.uri = line.split(" ")[1];

        while (!"".equals(line)) {
            line = reader.readLine();
            final String[] header = line.split(":");
            if (header.length == 2) {
                headers.put(header[0].trim(), header[1].trim());
            }
        }

        this.requestBody = parseRequestBody(reader);
    }

    public String getRequestBody(String key) {
        return requestBody.getOrDefault(key, "");
    }

    public String getMethod() {
        return method.name();
    }

    public boolean isPost() {
        return method.isPost();
    }

    public String getUri() {
        return uri;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    private Map<String, String> parseRequestBody(BufferedReader reader) throws IOException {
        int contentLength = getContentLength();
        char[] buffer = new char[contentLength];
        reader.read(buffer, 0, contentLength);
        return parseQueryString(new String(buffer));
    }

    private int getContentLength() {
        return Integer.parseInt(headers.getOrDefault("Content-Length", "0"));
    }

    private Map<String, String> parseQueryString(String queryString) {
        return Stream.of(queryString.split("&"))
                .map(param -> param.split("="))
                .collect(Collectors.toMap(param -> param[0], param -> param[1]));
    }
}
