package com.foocmend.controllers;

import com.foocmend.commons.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class Index {

    private final Utils utils;

    @GetMapping
    public String mainPage() {

        return utils.view("main/index");
    }
}
