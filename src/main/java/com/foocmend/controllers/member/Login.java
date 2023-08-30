package com.foocmend.controllers.member;

import com.foocmend.commons.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member/login")
@RequiredArgsConstructor
public class Login {
    private final Utils utils;
    @GetMapping
    public String login(Model model) {
        model.addAttribute("addCss", new String[] { "member/style"});
        return utils.view("member/login");
    }
}
