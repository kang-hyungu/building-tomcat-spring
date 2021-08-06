package nextstep.jwp.http;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HttpHeaders {

    private final Map<HttpHeader, String> headers;

    HttpHeaders() {
        this.headers = new ConcurrentHashMap<>();
    }

    void add(String header) {
        final String[] headerTokens = header.split(":");
        if (headerTokens.length == 2) {
            final String headerKey = headerTokens[0].trim();
            this.headers.put(HttpHeader.valueOf(headerKey), headerTokens[1].trim());
        }
    }

    String getHeader(HttpHeader httpHeader) {
        return headers.get(httpHeader);
    }

    private int getIntHeader(HttpHeader name) {
        String header = getHeader(name);
        return header == null ? 0 : Integer.parseInt(header);
    }

    int getContentLength() {
        return getIntHeader(HttpHeader.CONTENT_LENGTH);
    }

    HttpCookie getCookies() {
        return new HttpCookie(getHeader(HttpHeader.COOKIE));
    }

    HttpSession getSession() {
        return new HttpSession();
//        return HttpSessions.getSession(getCookies().getCookie("JSESSIONID"));
    }
}
