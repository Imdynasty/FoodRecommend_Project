package com.foocmend.controllers.search;

import com.foocmend.services.search.SearchHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
}
