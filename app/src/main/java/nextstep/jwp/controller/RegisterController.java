package nextstep.jwp.controller;

import nextstep.jwp.db.InMemoryUserRepository;
import nextstep.jwp.http.HttpRequest;
import nextstep.jwp.http.HttpResponse;
import nextstep.jwp.model.User;
import nextstep.jwp.mvc.AbstractController;

public class RegisterController extends AbstractController {

    @Override
    protected void doPost(HttpRequest request, HttpResponse response) {
        final User user = new User(2,
                request.getParameter("account"),
                request.getParameter("password"),
                request.getParameter("email"));
        InMemoryUserRepository.save(user);

        response.sendRedirect("index.html");
    }

    @Override
    protected void doGet(HttpRequest request, HttpResponse response) {
        response.forward("register.html");
    }
}
