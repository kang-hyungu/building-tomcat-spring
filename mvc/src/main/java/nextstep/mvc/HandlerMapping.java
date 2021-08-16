package nextstep.mvc;

import nextstep.mvc.controller.Controller;

public interface HandlerMapping {

    void initialize();

    Controller getHandler(String requestUri);
}
