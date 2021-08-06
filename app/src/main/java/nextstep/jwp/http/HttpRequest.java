package nextstep.jwp.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HttpRequest {

    private final RequestLine requestLine;
    private final Map<String, String> headers;
    private final Map<String, String> params;
    private final Map<String, String> requestBody;

    public HttpRequest(BufferedReader reader) throws IOException {
        String line = reader.readLine();
        this.requestLine = new RequestLine(line);

        this.headers = new ConcurrentHashMap<>();
        while (!"".equals(line)) {
            line = reader.readLine();
            parseHeader(line);
        }

        this.params = parseQueryString(requestLine.getQueryString());
        this.requestBody = parseRequestBody(reader);
    }

    private void parseHeader(String header) {
        final String[] headerTokens = header.split(":");
        if (headerTokens.length == 2) {
            this.headers.put(headerTokens[0].trim(), headerTokens[1].trim());
        }
    }

    private Map<String, String> parseRequestBody(BufferedReader reader) throws IOException {
        int contentLength = getContentLength();
        if (contentLength == 0) {
            return Collections.emptyMap();
        }

        char[] buffer = new char[contentLength];
        reader.read(buffer, 0, contentLength);
        return parseQueryString(new String(buffer));
    }

    private Map<String, String> parseQueryString(String queryString) {
        return Stream.of(queryString.split("&"))
                .map(param -> param.split("="))
                .collect(Collectors.toMap(param -> param[0], param -> param[1]));
    }

    public String getParameter(String name) {
        if (params.containsKey(name)) {
            return params.getOrDefault(name, "");
        }
        return requestBody.getOrDefault(name, "");
    }

    public boolean isPost() {
        return requestLine.isPost();
    }

    public String getPathInfo() {
        return requestLine.getPathInfo();
    }

    public int getContentLength() {
        return Integer.parseInt(headers.getOrDefault(HttpHeader.CONTENT_LENGTH.name(), "0"));
    }
}
