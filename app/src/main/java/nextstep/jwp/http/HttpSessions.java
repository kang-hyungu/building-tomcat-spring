package nextstep.jwp.http;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class HttpSessions {

    public static final String SESSION_ID_NAME = "JSESSIONID";

    private static final Map<String, HttpSession> SESSIONS = new ConcurrentHashMap<>();

    public static HttpSession getSession(String id) {
        return Optional.ofNullable(SESSIONS.get(id))
                .orElseGet(() -> {
                    final HttpSession session = new HttpSession(id);
                    return SESSIONS.put(id, session);
                });
    }

    static void remove(String id) {
        SESSIONS.remove(id);
    }

    private HttpSessions() {}
}
