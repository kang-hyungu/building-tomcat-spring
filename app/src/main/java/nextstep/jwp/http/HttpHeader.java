package nextstep.jwp.http;

public enum HttpHeader {

    CONTENT_LENGTH("Content-Length"),
    COOKIE("Cookie");

    private final String header;

    HttpHeader(String header) {
        this.header = header;
    }
}
