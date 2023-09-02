package com.foocmend.controllers.admin;

import com.foocmend.repositories.SiteVisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 관리자 페이지 메인
 *
 */
@Controller("adminIndex")
@RequestMapping("/admin")
@RequiredArgsConstructor
public class Index {

    private final SiteVisitRepository repository;

    @GetMapping
    public String index(Model model) {

        commonProcess(model);

        return "admin/main/index";
    }

    public void commonProcess(Model model) {

        //model.addAttribute("count", repository.count());

    }
}
