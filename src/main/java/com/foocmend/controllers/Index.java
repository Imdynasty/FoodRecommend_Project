package com.foocmend.controllers;

import com.foocmend.commons.Utils;
import com.foocmend.services.visit.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class Index {

    private final Utils utils;

    private final VisitService service;

    @GetMapping
    public String mainPage() {

        service.addVisit();

        return utils.view("main/index");
    }
}
