package nextstep.infra;

import nextstep.model.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUserRepository {

    private static final Map<String, User> database = new ConcurrentHashMap<>();

    static {
        final User user = new User(1, "hkkang", "password", "강현구");
        database.put(user.geUserId(), user);
    }

    public static void save(User user) {
        database.put(user.geUserId(), user);
    }

    public static User findByUserId(String userId) {
        return database.get(userId);
    }
}
