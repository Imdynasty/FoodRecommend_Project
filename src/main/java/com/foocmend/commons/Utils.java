package com.foocmend.commons;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 공통 유틸리티
 */
@Component
@RequiredArgsConstructor
public class Utils {

    private final HttpServletRequest request;
    private final HttpSession session;

    /**
     * 모바일 장비 접속 여부
     *
     * @return
     */
    public boolean isMobile() {
        boolean isMobile = request.getHeader("User-Agent").matches(".*(iPhone|iPod|iPad|BlackBerry|Android|Windows CE|LG|MOT|SAMSUNG|SonyEricsson).*");

        /** 세션에 device 값이 이미 있는 경우 대체 */
        String device = (String)session.getAttribute("device");
        if (device != null && device.equals("mobile")) isMobile = true;

        return isMobile;
    }

    /**
     * PC와 Mobile 구분에 따른 템플릿 prefix 처리
     *
     * @param tpl
     * @return
     */
    public String view(String tpl) {
        String prefix = isMobile() ? "mobile/":"front/";
        return prefix + tpl;
    }

}
