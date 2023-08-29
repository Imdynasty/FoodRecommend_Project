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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller("adminFoodManagement")
@RequestMapping("/admin/food")
@RequiredArgsConstructor
public class FoodManagement {

    private Logger logger = LoggerFactory.getLogger(FoodManagement.class);

    private final ChartService service;

    @RequestMapping(value = "/admin/food", method = RequestMethod.GET)
    public @ResponseBody List<Long> Chart(Model model) {

        List<Long> list = service.getMapper();

        logger.info("food" + list.toString());

        model.addAttribute("list", list);

        return list;
    }

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
