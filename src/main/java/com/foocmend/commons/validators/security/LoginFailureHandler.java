package com.foocmend.commons.validators.security;

import com.foocmend.services.member.exceptions.LoginValidationException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

public class LoginFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        HttpSession session = request.getSession();
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        session.removeAttribute("requiredEmail");
        session.removeAttribute("requiredPassword");
        session.removeAttribute("global");

        session.setAttribute("email", email);

        try {
            if (email == null || email.isBlank()) {
                throw new LoginValidationException("requiredEmail", "NotBlank.email");
            }

            if (password == null || password.isBlank()) {
                throw new LoginValidationException("requiredPassword", "NotBlank.password");
            }

            throw new LoginValidationException("globalError", "validation.login.fail");

        } catch (LoginValidationException e) {
            session.setAttribute(e.getField(), e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/member/login");
    }
}
