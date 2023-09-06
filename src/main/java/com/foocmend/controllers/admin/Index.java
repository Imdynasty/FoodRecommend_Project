package com.foocmend.controllers.admin;

import com.foocmend.repositories.SiteVisitRepository;
import com.foocmend.services.ChartService;
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

    private final ChartService service;

    @GetMapping
    public String index(Model model) {

        commonProcess(model);

        model.addAttribute("list", service.maleCount());
        model.addAttribute("list2", service.femaleCount());

        return "admin/main/index";
    }

    public void commonProcess(Model model) {

        model.addAttribute("count", repository.count());
        model.addAttribute("total", repository.total());

    }
}
