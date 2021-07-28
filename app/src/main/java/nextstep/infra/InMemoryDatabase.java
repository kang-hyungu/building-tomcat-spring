package nextstep.infra;

import nextstep.model.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryDatabase {

    private static final Map<Long, User> database = new ConcurrentHashMap<>();

    static {
        final User user = new User(1, "hhkkang@woowahan.com", "password", "강현구");
        database.put(user.getId(), user);
    }

    public static void save(User user) {
        database.put(user.getId(), user);
    }

    public static User findById(long id) {
        return database.get(id);
    }
}
