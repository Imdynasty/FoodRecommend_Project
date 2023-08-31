package com.foocmend.controllers.restaurant;

import com.foocmend.commons.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/restaurant")
@RequiredArgsConstructor
public class RestaurantFront {
    private final Utils utils;
    public String index() {
        return utils.view("restaurant/index");
    }
}
