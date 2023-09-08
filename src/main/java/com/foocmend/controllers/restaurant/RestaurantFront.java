package com.foocmend.controllers.restaurant;

import com.foocmend.commons.CommonProcess;
import com.foocmend.commons.ListData;
import com.foocmend.commons.ScriptExceptionProcess;
import com.foocmend.commons.Utils;
import com.foocmend.entities.Restaurant;
import com.foocmend.services.restaurant.SearchRestaurantService;
import com.foocmend.services.search.SearchHistoryService;
import com.foocmend.services.wishlist.SearchWishListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/restaurant")
@RequiredArgsConstructor
public class RestaurantFront implements CommonProcess, ScriptExceptionProcess {
    private final SearchRestaurantService searchService;
    private final SearchWishListService wishListService;
    private final SearchHistoryService historyService;
    private final Utils utils;

    @GetMapping
    public String index(@ModelAttribute RestaurantSearchForm search, Model model) {
        commonProcess(model, "search");

        ListData<Restaurant> data = searchService.getList(search);
        model.addAttribute("items", data.getContent());
        model.addAttribute("pagination", data.getPagination());
        return utils.view("restaurant/index2");
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable Long id, @ModelAttribute RestaurantSearchForm search, Model model) {
        commonProcess(model, "view");

        Restaurant item = searchService.get(id);
        search.setType(item.getType());

        // 조회 식당 최근 검색에 추가
        historyService.save(item.getStoreName());

        model.addAttribute("item", item);

        return utils.view("restaurant/view");
    }

    /**
     * 개인 위치 및 선호 검색어 기준 음식점 목록
     *
     * @param search
     * @param model
     * @return
     */
    @GetMapping("/my")
    public String my(RestaurantSearchForm search, Model model) {
        log.info(search.toString());
        ListData<Restaurant> data = searchService.getList(search);

        model.addAttribute("items", data.getContent());

        return utils.view("restaurant/my");
    }

    public void commonProcess(Model model, String mode) {
        commonProcess(model, mode, null);
    }

    public void commonProcess(Model model, String mode, String subTitle) {
        String pageTitle = "맛집 찾기";
        if (subTitle != null && !subTitle.isBlank()) pageTitle = subTitle + "-" + pageTitle;

        List<String> addCommonScript = new ArrayList<>();
        if (mode.equals("view")) {
            addCommonScript.add("map");
        }

        model.addAttribute("addCommonScript", addCommonScript);
        model.addAttribute("categories", searchService.getCategories());
        model.addAttribute("pageTitle", pageTitle);

        /** 찜하기 번호 추출 */
        model.addAttribute("wishIds",wishListService.getMyIds());
    }
}
