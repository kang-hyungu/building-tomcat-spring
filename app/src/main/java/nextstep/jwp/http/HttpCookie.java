package nextstep.jwp.http;

import com.google.common.base.Strings;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HttpCookie {

    private final Map<String, String> cookies;

    HttpCookie(String cookieValue) {
        this.cookies = parseCookies(cookieValue);
    }

    public String getCookie(String name) {
        return cookies.get(name);
    }

    private Map<String, String> parseCookies(String cookies) {
        if (Strings.isNullOrEmpty(cookies)) {
            return Collections.emptyMap();
        }
        return Stream.of(cookies.split(";"))
                .map(param -> param.split("="))
                .collect(Collectors.toMap(param -> param[0], param -> param[1]));
    }
}
