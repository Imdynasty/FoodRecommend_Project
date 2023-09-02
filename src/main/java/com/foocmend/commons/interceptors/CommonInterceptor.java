package com.foocmend.commons.interceptors;

import com.foocmend.commons.Utils;
import com.foocmend.services.search.SearchHistoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 공통 처리 인터셉터
 * 1. 로그인 실패시 세션에 남아 있는 데이터 삭제 
 * 2. 현재 접속한 장비 체크 하여 템플릿 분리 설정
 *
 */
@Component
@RequiredArgsConstructor
public class CommonInterceptor implements HandlerInterceptor {
    private final HttpServletRequest request;
    private final SearchHistoryService historyService;
    private final Utils utils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        init();

        return true;
    }

    private void init() {
        HttpSession session = request.getSession();
        String URI = request.getRequestURI();

        // 1. 로그인 실패시 세션에 남아 있는 데이터 삭제
        if (URI.indexOf("/member/login") == -1) { // 로그인이 아닌 경로로 접속한 경우 불필요 데이터 삭제
            session.removeAttribute("email");
            session.removeAttribute("requiredEmail");
            session.removeAttribute("requiredPassword");
            session.removeAttribute("globalError");
        }

        // 2. 현재 접속한 장비 체크 하여 템플릿 분리 설정
        String device = utils.isMobile()?"mobile":"pc";
        String _device = request.getParameter("device");

        // URL에 ?device=mobile이 있는 경우
        device = _device == null ? device : _device.equals("mobile") ? "mobile" : "pc";
        session.setAttribute("device", device);

        // 검색어 기록 추출
        request.setAttribute("searchHistories", historyService.getRecents());

    }
}
