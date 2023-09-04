package com.foocmend.controllers.admin;

import com.foocmend.commons.Menu;
import com.foocmend.commons.MenuDetail;
import com.foocmend.entities.EntityReview;
import com.foocmend.repositories.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller("adminBasicReview")
@RequestMapping("/admin/review")
@RequiredArgsConstructor
public class Review {

    private final ReviewRepository repository;

    @GetMapping
    public String config(Model model) {
        List<EntityReview> reviewList = repository.findAll();
        commonProcess(model);

        return "admin/basic/review";
    }


    @PostMapping
    public String save(Model model) {
        commonProcess(model);

        return "redirect:/admin/review";
    }

    @GetMapping("/list")
    public String findAll(Model model) {
        List<EntityReview> reviewList = repository.findAll();
        model.addAttribute("reviewList", reviewList);

        return "admin/basic/review";
    }



    private void commonProcess(Model model) {
        model.addAttribute("menuCode", "review");
        model.addAttribute("pageTitle", "리뷰 관리");
        List<MenuDetail> submenus = Menu.gets("member");
        model.addAttribute("submenus",submenus);
    }
}
