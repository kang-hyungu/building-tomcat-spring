package nextstep.jwp.http;

public class HttpConstants {

    private static final String CARRIAGE_RETURN = "\r";
    private static final String LINE_FEED = "\n";
    public static final String NEW_LINE = CARRIAGE_RETURN + LINE_FEED;

    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_LENGTH = "Content-Length";
    public static final String LOCATION = "Location";
    public static final String COOKIE = "Cookie";
    public static final String SET_COOKIE = "Set-Cookie";

    private HttpConstants() { }
}
