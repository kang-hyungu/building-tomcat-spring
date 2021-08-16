package com.techcourse.controller;

import nextstep.mvc.controller.asis.Controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class RegisterViewController implements Controller {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        return "/register.jsp";
    }
}
