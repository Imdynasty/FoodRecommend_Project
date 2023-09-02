package com.foocmend.services.search;

import com.foocmend.commons.Utils;
import com.foocmend.entities.QSearchHistory;
import com.foocmend.entities.SearchHistory;
import com.foocmend.entities.SearchHistoryId;
import com.foocmend.repositories.SearchHistoryRepository;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchHistoryService {

    private final Utils utils;
    private final SearchHistoryRepository repository;

    public void save(String keyword) {
        int uid = utils.getBrowserId();
        LocalDate today = LocalDate.now();
        SearchHistoryId id = new SearchHistoryId(uid, keyword, today);
        SearchHistory searchHistory = repository.findById(id)
                .orElseGet(() -> SearchHistory.builder()
                  .uid(uid)
                  .keyword(keyword)
                  .searchDate(today)
                  .build());
        searchHistory.setSearchCnt(searchHistory.getSearchCnt()+1);

        repository.saveAndFlush(searchHistory);
    }

    /**
     * 최근 검색어 조회
     *
     * 기본값은 최근 3일전
     * @return
     */
    public List<SearchHistory> getRecents(int days, int limit) {
        limit = Utils.getNumber(limit, 10);
        LocalDate date = LocalDate.now().minusDays(days);
        QSearchHistory searchHistory = QSearchHistory.searchHistory;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(searchHistory.uid.eq(utils.getBrowserId()))
                .and(searchHistory.searchDate.goe(date));

        Pageable pageable = PageRequest.of(0, limit);
        Page<SearchHistory> data = repository.findAll(builder, pageable);

        return data.getContent();
    }


    public List<SearchHistory> getRecents() {
        return getRecents(3, 10);
    }

    public List<SearchHistory> getRecents(int days) {
        return getRecents(days,10);
    }
}
