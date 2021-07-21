package nextstep.jwp.http;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HttpRequest {

    private final RequestLine requestLine;

    public HttpRequest(final String request) {
        Objects.requireNonNull(request);
        final List<String> splitRequest = Stream.of(request.split(Constants.NEW_LINE))
                .collect(Collectors.toList());

        this.requestLine = new RequestLine(splitRequest.get(0));
    }

    public boolean isCss() {
        return requestLine.isCss();
    }
}
