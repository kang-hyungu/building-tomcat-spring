package nextstep.jwp.http;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Bytes;

import java.nio.charset.StandardCharsets;

public class HttpResponse {

    private final boolean css;
    private final byte[] responseBody;

    public HttpResponse(boolean css, byte[] responseBody) {
        this.css = css;
        this.responseBody = Preconditions.checkNotNull(responseBody);
    }

    public byte[] toBytes() {
        final String httpHeader = String.join(Constants.NEW_LINE,
                "HTTP/1.1 200 OK",
                css ? "Content-Type: text/css;charset=utf-8" : "Content-Type: text/html;charset=utf-8",
                "Content-Length: " + responseBody.length,
                "",
                "");

        return Bytes.concat(httpHeader.getBytes(StandardCharsets.UTF_8), responseBody);
    }
}
