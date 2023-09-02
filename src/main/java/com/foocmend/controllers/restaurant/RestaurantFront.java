package com.foocmend.controllers.restaurant;

import com.foocmend.commons.*;
import com.foocmend.entities.Restaurant;
import com.foocmend.services.restaurant.SearchRestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/restaurant")
@RequiredArgsConstructor
public class RestaurantFront implements CommonProcess, ScriptExceptionProcess {
    private final SearchRestaurantService searchService;
    private final Utils utils;

    @GetMapping
    public String index(@ModelAttribute RestaurantSearchForm search, Model model) {
        commonProcess(model, "search");

        ListData<Restaurant> data = searchService.getList(search);
        model.addAttribute("items", data.getContent());
        model.addAttribute("pagination", data.getPagination());
        return utils.view("restaurant/index2");
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable Long id, @ModelAttribute RestaurantSearchForm search, Model model) {
        commonProcess(model, "view");

        Restaurant item = searchService.get(id);
        search.setType(item.getType());

        model.addAttribute("item", item);

        return utils.view("restaurant/view");
    }

    public void commonProcess(Model model, String mode) {
        String pageTitle = "맛집 찾기";

        List<String> addCommonScript = new ArrayList<>();
        if (mode.equals("view")) {
            addCommonScript.add("map");
        }

        model.addAttribute("addCommonScript", addCommonScript);
        model.addAttribute("categories", searchService.getCategories());
        model.addAttribute("pageTitle", pageTitle);
    }
}
