package nextstep.model;

public class User {

    private final long id;
    private final String userId;
    private final String password;
    private final String name;

    public User(long id, String userId, String password, String name) {
        this.id = id;
        this.userId = userId;
        this.password = password;
        this.name = name;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public String geUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
