package nextstep.jwp.http;

import java.util.stream.Stream;

public enum HttpMethod {

    GET,
    POST;

    public static HttpMethod of(String method) {
        return Stream.of(values())
                .filter(value -> value.name().equals(method) )
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public  boolean isPost() {
        return this == POST;
    }
}
