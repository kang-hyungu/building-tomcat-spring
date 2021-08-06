package nextstep.jwp.http;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RequestLineTest {

    @Test
    void requestLine() {
        final RequestLine requestLine = new RequestLine("GET /index.html HTTP/1.1 ");

        assertThat(requestLine.getMethod()).isEqualTo(HttpMethod.GET);
        assertThat(requestLine.getPathInfo()).isEqualTo("/index.html");
        assertThat(requestLine.getVersion()).isEqualTo("HTTP/1.1");
        assertThat(requestLine.getQueryString()).isBlank();
    }

    @Test
    void queryString() {
        final RequestLine requestLine = new RequestLine("GET /login?account=gugu&password=password HTTP/1.1 ");

        assertThat(requestLine.getMethod()).isEqualTo(HttpMethod.GET);
        assertThat(requestLine.getPathInfo()).isEqualTo("/login");
        assertThat(requestLine.getVersion()).isEqualTo("HTTP/1.1");
        assertThat(requestLine.getQueryString()).isEqualTo("account=gugu&password=password");
    }
}
