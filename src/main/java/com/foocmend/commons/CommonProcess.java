package com.foocmend.commons;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;

import java.util.List;

public interface CommonProcess {
    default void commonProcess(Model model, String pageTitle, String menuCode, HttpServletRequest request) {
        model.addAttribute("pageTitle", pageTitle);

        List<MenuDetail> submenus = Menu.gets(menuCode);
        model.addAttribute("submenus", submenus);

        String subMenuCode = Menu.getSubMenuCode(request);
        model.addAttribute("menuCode", menuCode);
        model.addAttribute("subMenuCode", subMenuCode);
    }
}
