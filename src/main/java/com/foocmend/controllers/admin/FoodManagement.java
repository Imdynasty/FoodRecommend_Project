package com.foocmend.controllers.admin;

import com.foocmend.services.ChartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller("adminFoodManagement")
@RequestMapping("/admin/food")
@RequiredArgsConstructor
public class FoodManagement {

    private final ChartService service;

    List<Long> list = new ArrayList<>();


    @GetMapping
    public String food(Model model) {
        commonProcess(model);
        list = service.getMapper();
        model.addAttribute("list", list);

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
