package nextstep.jwp.mvc;

import nextstep.jwp.controller.ForwardController;
import nextstep.jwp.controller.LoginController;
import nextstep.jwp.controller.RegisterController;
import nextstep.jwp.controller.StaticController;
import nextstep.jwp.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RequestMapping {

    private static final Logger log = LoggerFactory.getLogger(RequestMapping.class);

    private static final Map<String, Controller> controllers = new ConcurrentHashMap<>();
    private static final Controller forwardController;
    private static final Controller staticController;

    static {
        controllers.put("/login", new LoginController());
        controllers.put("/register", new RegisterController());
        forwardController = new ForwardController();
        staticController = new StaticController();
    }

    public Controller getController(HttpRequest request) {
        final String path = getDefaultPath(request.getPathInfo());
        log.debug("Request Mapping Url : {}", path);

        final Controller controller = controllers.get(path);
        if (controller != null) {
            return controller;
        }

        if (path.endsWith(".html")) {
            return forwardController;
        }

        return staticController;
    }

    private String getDefaultPath(String path) {
        if (path.equals("/")) {
            return "/index.html";
        }
        return path;
    }
}
