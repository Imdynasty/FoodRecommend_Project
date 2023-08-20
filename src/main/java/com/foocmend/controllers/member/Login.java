package com.foocmend.controllers.member;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member/login")
public class Login {
    @GetMapping
    public String login() {

        return "front/member/login";
    }
}
