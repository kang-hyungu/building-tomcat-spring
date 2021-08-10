package nextstep.jwp;

import nextstep.jwp.mvc.RequestMapping;

public class JwpApplication {

    public static void main(String[] args) {
        final int port = WebServer.defaultPortIfNull(args);
        final RequestMapping requestMapping = new RequestMapping();

        final WebServer webServer = new WebServer(port, requestMapping);
        webServer.run();
    }
}
