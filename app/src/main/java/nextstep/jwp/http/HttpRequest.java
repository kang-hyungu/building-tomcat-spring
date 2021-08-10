package nextstep.jwp.http;

import java.io.BufferedReader;
import java.io.IOException;

public class HttpRequest {

    private final RequestLine requestLine;
    private final HttpHeaders headers;
    private final RequestParams requestParams;

    public HttpRequest(BufferedReader reader) throws IOException {
        this.requestLine = new RequestLine(reader.readLine());
        this.headers = processHeaders(reader);
        this.requestParams = new RequestParams(requestLine.getQueryString());
        this.requestParams.addBody(readRequestBody(reader));
    }

    private HttpHeaders processHeaders(BufferedReader reader) throws IOException {
        final HttpHeaders httpHeaders = new HttpHeaders();
        String line;
        while (!(line = reader.readLine()).equals("")) {
            httpHeaders.add(line);
        }
        return httpHeaders;
    }

    private String readRequestBody(BufferedReader reader) throws IOException {
        int contentLength = headers.getContentLength();
        char[] buffer = new char[contentLength];
        reader.read(buffer, 0, contentLength);
        return new String(buffer);
    }

    public String getParameter(String name) {
        return requestParams.getParameter(name);
    }

    public boolean isPost() {
        return requestLine.isPost();
    }

    public String getPathInfo() {
        return requestLine.getPathInfo();
    }

    public HttpMethod getMethod() {
        return requestLine.getMethod();
    }

    public HttpSession getSession() {
        return headers.getSession();
    }

    public HttpCookie getCookies() {
        return headers.getCookies();
    }
}
