package com.foocmend.controllers.admin;

import com.foocmend.entities.Member;
import com.foocmend.repositories.ChartMapper;
import com.foocmend.services.ChartService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller("adminFoodManagement")
@RequestMapping("/admin/food")
@RequiredArgsConstructor
public class FoodManagement {

    private final ChartService service;

    List<Long> list = new ArrayList<>();


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
        list = service.getMapper();
        model.addAttribute("pageTitle", "맛집 관리");
        model.addAttribute("menuCode", "food");
        model.addAttribute("list", list);
    }

}
