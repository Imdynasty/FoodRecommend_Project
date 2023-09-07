package com.foocmend.controllers.search;

import com.foocmend.services.search.SearchHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchHistoryController {

    private final SearchHistoryService historyService;

    @ResponseBody
    @PostMapping("/keyword/save")
    public void saveKeyword(String keyword) {

        historyService.save(keyword);
    }

    @ResponseBody
    @RequestMapping("/keywords")
    public List<String> searchKeyword(SearchHistoryForm form) {
        List<String> items = historyService.getList(form);

        return items;
    }
}
