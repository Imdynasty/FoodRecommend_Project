package com.foocmend.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 관리자 페이지 메인
 *
 */
@Controller("adminIndex")
@RequestMapping("/admin")
public class Index {
    @GetMapping
    public String index() {
        return "admin/main/index";
    }
}
