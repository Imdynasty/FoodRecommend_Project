package com.foocmend.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("adminBasicReview")
@RequestMapping("/admin/review")
public class Review {
    @GetMapping
    public String config(Model model) {
        commonProcess(model);

        return "admin/basic/review";
    }

    @PostMapping
    public String save(Model model) {
        commonProcess(model);

        return "redirect:/admin/review";
    }

    private void commonProcess(Model model) {
        model.addAttribute("menuCode", "review");
        model.addAttribute("pageTitle", "리뷰 관리");
    }
}
