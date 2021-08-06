package nextstep.jwp.http;

import com.google.common.base.Preconditions;

import java.util.StringTokenizer;

public class RequestLine {

    private static final int REQUEST_LINE_SIZE = 3;

    private final HttpMethod method;
    private final String path;
    private final String version;
    private final String queryString;

    public RequestLine(String requestLine) {
        Preconditions.checkNotNull(requestLine);
        final StringTokenizer requestLineTokenizer = new StringTokenizer(requestLine, " ");
        Preconditions.checkArgument(requestLineTokenizer.countTokens() == REQUEST_LINE_SIZE);

        this.method = HttpMethod.valueOf(requestLineTokenizer.nextToken());
        final StringTokenizer uriTokenizer = new StringTokenizer(requestLineTokenizer.nextToken(), "?");
        this.path = uriTokenizer.nextToken();
        this.queryString = defaultIfNoMoreTokens(uriTokenizer);
        this.version = requestLineTokenizer.nextToken();
    }

    private String defaultIfNoMoreTokens(StringTokenizer uriTokenizer) {
        if (uriTokenizer.hasMoreTokens()) {
            return uriTokenizer.nextToken();
        } else {
            return "";
        }
    }

    public boolean isPost() {
        return method.isPost();
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPathInfo() {
        return path;
    }

    public String getVersion() {
        return version;
    }

    public String getQueryString() {
        return queryString;
    }
}
