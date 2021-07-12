package nextstep.jwp;

public class JwpApplication {

    public static void main(String[] args) {
        final WebServer webServer = new WebServer(8080, 1000);
        webServer.run();
    }
}
