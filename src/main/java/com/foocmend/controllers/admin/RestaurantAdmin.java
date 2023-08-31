package com.foocmend.controllers.admin;

import com.foocmend.commons.*;
import com.foocmend.controllers.restaurant.RestaurantSearchForm;
import com.foocmend.entities.Restaurant;
import com.foocmend.services.restaurant.DeleteRestaurantService;
import com.foocmend.services.restaurant.SaveRestaurantService;
import com.foocmend.services.restaurant.SearchRestaurantService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/admin/restaurant")
@RequiredArgsConstructor
public class RestaurantAdmin implements CommonProcess, ScriptExceptionProcess {
    private final SearchRestaurantService searchService;
    private final SaveRestaurantService saveService;
    private final DeleteRestaurantService deleteService;
    private final HttpServletRequest request;

    /**
     * 음식점 목록
     *
     */
    @GetMapping
    public String index(RestaurantSearchForm search, Model model) {
        commonProcess(model, "list");
        ListData<Restaurant> data = searchService.getList(search);

        model.addAttribute("items", data.getContent());
        model.addAttribute("pagination", data.getPagination());

        return "admin/restaurant/index";
    }

    @PostMapping
    public String indexPs(RestaurantForm form, Model model) {
        commonProcess(model, "list");

        deleteService.delete(form);

        model.addAttribute("script", "parent.location.reload();");
        return "commons/execute_script";
    }
    

    /**
     * 음식점 정보 등록
     *
     */
    @GetMapping("/register")
    public String register(@ModelAttribute RestaurantForm form, Model model) {
        commonProcess(model, "register");

        return "admin/restaurant/register";
    }

    /**
     * 음식점정보 수정
     *
     */
    @GetMapping("/update/{id}")
    public String update(@PathVariable Long id, Model model) {
        commonProcess(model, "update");
        RestaurantForm form = searchService.getForm(id);

        model.addAttribute("restaurantForm", form);

        return "admin/restaurant/update";
    }

    /**
     * 음식점 등록/수정 처리
     *
     * @return
     */
    @PostMapping("/save")
    public String save(@Valid RestaurantForm form, Errors errors, Model model) {
        commonProcess(model, "save");
        String mode = Objects.requireNonNullElse(form.getMode(), "register");

        if (errors.hasErrors()) {
            return "admin/restaurant/" + mode;
        }

        try {
            saveService.save(form);
        } catch (CommonException e) {
            e.printStackTrace();
            throw new AlertBackException(e.getMessage());
        }

        return "redirect:/admin/restaurant";
    }
    public void commonProcess(Model model, String mode) {
        String pageTitle="음식점 목록";
        mode = Objects.requireNonNullElse(mode, "list");

        CommonProcess.super.commonProcess(model, pageTitle,"restaurant", request);

        List<String> addCommonScript = new ArrayList<>();
        List<String> addScript = new ArrayList<>();

        /** 음식점 정보 등록/수정 공통 스크립트 추가 */
        if (mode.equals("register") || mode.equals("update") || mode.equals("save")) {
            addCommonScript.add("address");
            addCommonScript.add("fileManager");
            addCommonScript.add("ckeditor/ckeditor");
            addCommonScript.add("map");
            addScript.add("restaurant/form");
        }

        model.addAttribute("addCommonScript", addCommonScript);
        model.addAttribute("addScript", addScript);
        model.addAttribute("categories", searchService.getCategories());
    }
}
