package nextstep.jwp;

public class JwpApplication {
    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) {
        System.out.println("pull request");
        System.out.println(new JwpApplication().getGreeting());
    }
}