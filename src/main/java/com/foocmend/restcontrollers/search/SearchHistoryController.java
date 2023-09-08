package com.foocmend.restcontrollers.search;

import com.foocmend.services.search.SearchHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchHistoryController {

    private final SearchHistoryService historyService;

    @PostMapping("/keyword/save")
    public void saveKeyword(String keyword) {

        historyService.save(keyword);
    }

    @RequestMapping("/keywords")
    public List<String> searchKeyword(SearchHistoryForm form) {
        List<String> items = historyService.getList(form);

        return items;
    }

    @GetMapping("/remove/{keyword}")
    public void removeKeyword(@PathVariable String keyword) {
        historyService.remove(keyword);
    }
}
