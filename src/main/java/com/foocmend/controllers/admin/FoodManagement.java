package com.foocmend.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("adminFoodManagement")
@RequestMapping("/admin/food")
public class FoodManagement {

    @GetMapping
    public String food(Model model) {
        commonProcess(model);

        return "admin/basic/food";
    }

    @PostMapping
    public String save(Model model) {
        commonProcess(model);

        return "redirect:/admin/food";
    }

    private void commonProcess(Model model) {
        model.addAttribute("pageTitle", "맛집 관리");
        model.addAttribute("menuCode", "food");
    }

}
