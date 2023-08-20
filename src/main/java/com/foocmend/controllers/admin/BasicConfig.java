package com.foocmend.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("adminBasicConfig")
@RequestMapping("/admin/config")
public class BasicConfig {
    @GetMapping
    public String config(Model model) {
        commonProcess(model);

        return "admin/basic/index";
    }

    @PostMapping
    public String save(Model model) {
        commonProcess(model);

        return "redirect:/admin/config";
    }

    private void commonProcess(Model model) {
        model.addAttribute("menuCode", "config");
        model.addAttribute("pageTitle", "사이트 설정");
    }
}
