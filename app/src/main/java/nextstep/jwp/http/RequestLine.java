package nextstep.jwp.http;

import nextstep.jwp.FilenameExtension;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RequestLine {

    public final String httpMethod;
    public final String path;
    public final String httpVersion;

    public RequestLine(String requestLine) {
        Objects.requireNonNull(requestLine);
        final List<String> splitRequestLine = Stream.of(requestLine.split(Constants.SPACE))
                .collect(Collectors.toList());

        this.httpMethod = splitRequestLine.get(0);
        this.path = splitRequestLine.get(1);
        this.httpVersion = splitRequestLine.get(2);
    }

    public boolean isCss() {
        return path.endsWith(FilenameExtension.CSS);
    }
}
