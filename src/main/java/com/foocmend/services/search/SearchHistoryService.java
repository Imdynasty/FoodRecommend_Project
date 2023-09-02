package com.foocmend.services.search;

import com.foocmend.commons.Utils;
import com.foocmend.entities.QSearchHistory;
import com.foocmend.entities.SearchHistory;
import com.foocmend.entities.SearchHistoryId;
import com.foocmend.repositories.SearchHistoryRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchHistoryService {

    private final Utils utils;
    private final SearchHistoryRepository repository;

    @PersistenceContext
    private EntityManager em;
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


        JPAQueryFactory factory = new JPAQueryFactory(em);
        List<SearchHistory> items = factory.selectFrom(searchHistory)
                .where(builder)
                .offset(0)
                .limit(limit)
                .groupBy(searchHistory.keyword)
                .fetch();

        return items;
    }


    public List<SearchHistory> getRecents() {
        return getRecents(3, 10);
    }

    public List<SearchHistory> getRecents(int days) {
        return getRecents(days,10);
    }
}
