package com.foocmend.commons;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.ResourceBundle;

/**
 * 공통 유틸리티
 */
@Component
@RequiredArgsConstructor
public class Utils {

    private static ResourceBundle bundle;
    private static ResourceBundle bundleValidation;
    private static ResourceBundle bundleError;

    private final HttpServletRequest request;
    private final HttpSession session;
    private final MemberUtil memberUtil;

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
    /**
     * 메세지 조회
     *
     * @param code : 메세지 코드
     * @param type : validation, error, common
     * @return
     */
    public static String getMessage(String code, String type) {
        ResourceBundle _bundle = null;
        if (type.equals("validation")) { // 검증
            if (bundleValidation == null) bundleValidation = ResourceBundle.getBundle("messages.validations");
            _bundle = bundleValidation;
        } else if (type.equals("error")) { // 에러
            if (bundleError == null) bundleError = ResourceBundle.getBundle("messages.errors");
            _bundle = bundleError;
        } else { // 공통
            if (bundle == null) bundle = ResourceBundle.getBundle("messages.commons");
            _bundle = bundle;
        }

        try {
            return _bundle.getString(code);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 단일 요청 데이터 조회
     *
     */
    public String getParam(String name) {
        return request.getParameter(name);
    }

    /**
     * 복수개 요청 데이터 조회
     *
     */
    public String[] getParams(String name) {
        return request.getParameterValues(name);
    }


    public static int getNumber(int num, int defaultValue) {
        return num <= 0 ? defaultValue : num;
    }

    /**
     * 브라우저 ID
     * @return
     */
    public int getBrowserId() {
        long memNo = memberUtil.isLogin() ? memberUtil.getMember().getMemNo() : 0;
        String ua = request.getHeader("User-Agent");
        String ip = request.getRemoteAddr();

        return Objects.hash(memNo, ua, ip);
    }
}
