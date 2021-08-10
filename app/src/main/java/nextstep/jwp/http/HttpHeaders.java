package nextstep.jwp.http;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HttpHeaders {

    private final Map<String, String> headers;

    HttpHeaders() {
        this.headers = new ConcurrentHashMap<>();
    }

    void add(String header) {
        final String[] headerTokens = header.split(":");
        if (headerTokens.length == 2) {
            this.headers.put(headerTokens[0].trim(), headerTokens[1].trim());
        }
    }

    void put(String key, String value) {
        this.headers.put(key, value);
    }

    String value() {
        final StringBuilder stringBuilder = new StringBuilder();
        headers.forEach((key, value) ->
            stringBuilder.append(key).append(":").append(value)
                    .append(HttpConstants.NEW_LINE)
        );
        return stringBuilder.toString();
    }

    String getHeader(String httpHeader) {
        return headers.get(httpHeader);
    }

    private int getIntHeader(String name) {
        String header = getHeader(name);
        return header == null ? 0 : Integer.parseInt(header);
    }

    int getContentLength() {
        return getIntHeader(HttpConstants.CONTENT_LENGTH);
    }

    HttpCookie getCookies() {
        return new HttpCookie(getHeader(HttpConstants.COOKIE));
    }

    public HttpSession getSession() {
        return HttpSessions.getSession(getCookies().getCookie(HttpSessions.JSESSIONID));
    }
}
