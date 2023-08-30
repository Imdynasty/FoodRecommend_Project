package com.foocmend.commons;

import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

public class Menu {
    public static List<MenuDetail> gets(String code) {
        List<MenuDetail> menus = new ArrayList<>();

        // 회원 하위 메뉴
        if (code.equals("member")) {
            menus.add(new MenuDetail("member", "회원 목록", "/admin/member"));
        }

        // 게시판 하위 메뉴
        if (code.equals("board")) {
            menus.add(new MenuDetail("board", "게시판 목록", "/admin/board"));
            menus.add(new MenuDetail("register", "게시판 등록/수정", "/admin/board/register"));
            menus.add(new MenuDetail("posts", "게시글 관리", "/admin/board/posts"));
        }

        // 음식점 관리 하위 메뉴
        if (code.equals("restaurant")) {
            menus.add(new MenuDetail("restaurant", "음식점 목록", "/admin/restaurant"));
            menus.add(new MenuDetail("register", "음식점 등록/수정", "/admin/restaurant/register"));
        }

        return menus;
    }

    public static String getSubMenuCode(HttpServletRequest request) {
        String URI = request.getRequestURI();

        return URI.substring(URI.lastIndexOf('/') + 1);
    }
}
