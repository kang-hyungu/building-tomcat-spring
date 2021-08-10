package nextstep.jwp.controller;

import nextstep.jwp.http.HttpRequest;
import nextstep.jwp.http.HttpResponse;
import nextstep.jwp.mvc.AbstractController;

public class ForwardController extends AbstractController {

    @Override
    public void service(HttpRequest request, HttpResponse response) {
        final String path = getDefaultPath(request.getPathInfo());
        response.forward(path);
    }

    private String getDefaultPath(String path) {
        if (path.equals("/") || path.endsWith("index.html")) {
            return "index.html";
        }
        return path;
    }
}
