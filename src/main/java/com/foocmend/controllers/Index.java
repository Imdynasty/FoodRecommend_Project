package com.foocmend.controllers;

import com.foocmend.commons.Utils;
import com.foocmend.services.visit.VisitService;
import com.foocmend.services.wishlist.SearchWishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class Index {

    private final Utils utils;

    private final VisitService service;
    private final SearchWishListService wishListService;

    @GetMapping
    public String mainPage(Model model) {

        service.addVisit();
        /** 찜하기 번호 추출 */
        model.addAttribute("wishIds",wishListService.getMyIds());
        return utils.view("main/index");
    }
}
