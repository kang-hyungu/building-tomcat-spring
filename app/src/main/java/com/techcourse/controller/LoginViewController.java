package com.techcourse.controller;

import com.techcourse.domain.User;
import nextstep.mvc.controller.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class LoginViewController implements Controller {

    private static final Logger log = LoggerFactory.getLogger(LoginViewController.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        final User user = getUser(request.getSession());
        if (user != null) {
            log.info("logged in {}", user.getAccount());
            return "redirect:/index.jsp";
        }
        return "/login.jsp";
    }

    private User getUser(HttpSession session) {
        return (User) session.getAttribute("user");
    }
}
