package nextstep.jwp.http;

public enum StatusLine {

    OK("HTTP/1.1 200 OK "),
    FOUND("HTTP/1.1 302 Found "),
    NOT_FOUND("HTTP/1.1 404 Not Found ")
    ;

    private final String statusLine;

    StatusLine(String statusLine) {
        this.statusLine = statusLine;
    }

    public String withNewLine() {
        return statusLine + HttpConstants.NEW_LINE;
    }
}
