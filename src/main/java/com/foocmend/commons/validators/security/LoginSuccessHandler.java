package com.foocmend.commons.validators.security;

import com.foocmend.services.member.DetailMember;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        HttpSession session = request.getSession();
        session.removeAttribute("email");
        session.removeAttribute("requiredEmail");
        session.removeAttribute("requiredPassword");
        session.removeAttribute("globalError");

        /** 편의상 회원 정보를 세션에 기록 */
        DetailMember detailMember = (DetailMember) authentication.getPrincipal();
        session.setAttribute("detailMember", detailMember);

        /** redirectURL 값이 있으면 지정된 URL로 로그인 후 이동, 없다면 메인 페이지로 이동 */
        String redirectUrl = request.getParameter("redirectURL");
        String url = redirectUrl == null || redirectUrl.isBlank() ? request.getContextPath() + "/" : request.getContextPath() + redirectUrl;

        response.sendRedirect(url);
    }
}
