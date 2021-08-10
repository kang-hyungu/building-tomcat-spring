package nextstep.jwp.controller;

import nextstep.jwp.http.HttpRequest;
import nextstep.jwp.http.HttpResponse;
import nextstep.jwp.mvc.AbstractController;

public class StaticController extends AbstractController {

    @Override
    protected void doGet(HttpRequest request, HttpResponse response) {
        final String path = request.getPathInfo();
        final byte[] body = response.read(path);
        if (body == null || body.length <= 0) {
            response.responseNotFound();
            return;
        }

        response.addHeader("Content-Type", getContentType(path));
        response.addHeader("Content-Length", body.length);
        response.responseOk();
        response.responseBody(body);
    }

    private String getContentType(String path) {
        if (path.endsWith(".css")) {
            return "text/css;charset=utf-8;";
        } else if (path.endsWith(".js")) {
            return "application/javascript;";
        } else {
            return "text/html;charset=utf-8;";
        }
    }
}
