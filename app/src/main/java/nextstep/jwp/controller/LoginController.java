package nextstep.jwp.controller;

import nextstep.jwp.db.InMemoryUserRepository;
import nextstep.jwp.http.HttpRequest;
import nextstep.jwp.http.HttpResponse;
import nextstep.jwp.http.HttpSession;
import nextstep.jwp.model.User;
import nextstep.jwp.mvc.AbstractController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class LoginController extends AbstractController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @Override
    protected void doPost(HttpRequest request, HttpResponse response) {
        final Optional<User> user = InMemoryUserRepository.findByAccount(request.getParameter("account"));

        if (user.isPresent()) {
            final User found = user.get();
            log.info("User : {}", found);

            if (found.checkPassword(request.getParameter("password"))) {
                final HttpSession httpSession = request.getSession();
                httpSession.setAttribute("user", found);
                response.sendRedirect("index.html");
            }
        }

        response.sendRedirect("401.html");
    }

    @Override
    protected void doGet(HttpRequest request, HttpResponse response) {
        final User user = getUser(request.getSession());
        if (user == null) {
            response.forward("login.html");
            return;
        }
        log.info("logged in {}", user.getAccount());
        response.sendRedirect("index.html");
    }

    private User getUser(HttpSession session) {
        return (User) session.getAttribute("user");
    }
}
