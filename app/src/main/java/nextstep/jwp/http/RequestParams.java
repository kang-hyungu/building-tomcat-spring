package nextstep.jwp.http;

import com.google.common.base.Strings;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RequestParams {

    private final Map<String, String> params = new ConcurrentHashMap<>();

    public RequestParams(String queryString) {
        putParams(queryString);
    }

    public void addQueryString(String queryString) {
        putParams(queryString);
    }

    private void putParams(String param) {
        if (Strings.isNullOrEmpty(param)) {
            return;
        }

        params.putAll(parseQueryString(param));
    }

    private Map<String, String> parseQueryString(String queryString) {
        return Stream.of(queryString.split("&"))
                .map(param -> param.split("="))
                .collect(Collectors.toMap(param -> param[0], param -> param[1]));
    }

    public void addBody(String body) {
        putParams(body);
    }

    public String getParameter(String name) {
        return params.getOrDefault(name, "");
    }
}
