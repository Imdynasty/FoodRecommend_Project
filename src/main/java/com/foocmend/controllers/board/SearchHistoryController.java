package com.foocmend.controllers.board;

import com.foocmend.entities.SearchHistory;
import com.foocmend.repositories.SearchHistoryRepository;
import com.foocmend.services.board.SearchHistoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
@Controller
@RequestMapping("/search-history")
public class SearchHistoryController {

    private final SearchHistoryService searchHistoryService;
    private final SearchHistoryRepository searchHistoryRepository;

    public SearchHistoryController(SearchHistoryRepository searchHistoryRepository, SearchHistoryService searchHistoryService) {

        this.searchHistoryService = searchHistoryService;
        this.searchHistoryRepository = searchHistoryRepository;
    }

    @GetMapping
    public List<SearchHistory> getRecentSearchHistory() {
        // 최근 검색 기록을 조회하여 반환
        return searchHistoryRepository.findAll();
    }
    @GetMapping("/")
    public String home(Model model) {
        List<SearchHistory> recentSearches =  searchHistoryService.getRecentSearchHistory();
                model.addAttribute("recentSearches", recentSearches);
        return "header"; // Your Thymeleaf template name
    }




    /*@GetMapping
       public String showRecentSearchHistory(Model model) {
        List<SearchHistory> recentSearches = searchHistoryService.getRecentSearchHistory();
        model.addAttribute("recentSearches", recentSearches);
        return "front/outlines/search-history";  // Thymeleaf 템플릿 파일명
    }*/

}
