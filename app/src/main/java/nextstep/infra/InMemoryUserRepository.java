package nextstep.infra;

import nextstep.model.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUserRepository {

    private static final Map<String, User> database = new ConcurrentHashMap<>();

    static {
        final User user = new User(1, "hkkang@woowahan.com", "password", "강현구");
        database.put(user.getEmail(), user);
    }

    public static void save(User user) {
        database.put(user.getEmail(), user);
    }

    public static User findByEmail(String email) {
        return database.get(email);
    }
}
