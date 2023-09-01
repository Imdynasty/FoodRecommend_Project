package com.foocmend.controllers.restaurant;

import com.foocmend.commons.CommonProcess;
import com.foocmend.commons.ListData;
import com.foocmend.commons.ScriptExceptionProcess;
import com.foocmend.commons.Utils;
import com.foocmend.entities.Restaurant;
import com.foocmend.services.restaurant.SearchRestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/restaurant")
@RequiredArgsConstructor
public class RestaurantFront implements CommonProcess, ScriptExceptionProcess {
    private final SearchRestaurantService searchService;
    private final Utils utils;

    @GetMapping
    public String index(RestaurantSearchForm search, Model model) {
        commonProcess(model, "search");
        String type = search.getType();
        ListData<Restaurant> data = searchService.getList(search);
        model.addAttribute("items", data.getContent());
        model.addAttribute("pagination", data.getPagination());
        return utils.view("restaurant/index2");
    }

    public void commonProcess(Model model, String mode) {
        String pageTitle = "맛집 찾기";


        model.addAttribute("categories", searchService.getCategories());
        model.addAttribute("pageTitle", pageTitle);
    }
}
