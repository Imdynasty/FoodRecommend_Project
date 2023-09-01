package com.foocmend.services.board;

import com.foocmend.entities.SearchHistory;
import com.foocmend.repositories.SearchHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service

public class SearchHistoryService {

    private final SearchHistoryRepository searchHistoryRepository;

    public SearchHistoryService(SearchHistoryRepository searchHistoryRepository) {
        this.searchHistoryRepository = searchHistoryRepository;
    }

    public List<SearchHistory> getRecentSearchHistory() {
        return searchHistoryRepository.findAll();
    }

    public void saveSearchHistory(String keyword) {
        SearchHistory history = new SearchHistory();
        history.setKeyword(keyword);
        history.setSearchDate(LocalDateTime.now());
        searchHistoryRepository.save(history);

        // 로그 출력
        System.out.println("검색 기록 저장됨: " + history.getKeyword());
    }
}
