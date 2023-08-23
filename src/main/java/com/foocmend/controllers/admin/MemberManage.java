package com.foocmend.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("adminBasicMemberManage")
@RequestMapping("/admin/board")
public class MemberManage {
    @GetMapping
    public String config(Model model) {
        commonProcess(model);

        return "admin/basic/board";
    }

    @PostMapping
    public String save(Model model) {
        commonProcess(model);

        return "redirect:/admin/board";
    }

    private void commonProcess(Model model) {
        model.addAttribute("menuCode", "board");
        model.addAttribute("pageTitle", "회원관리");
    }
}
