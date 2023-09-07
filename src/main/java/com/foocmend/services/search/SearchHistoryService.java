package com.foocmend.services.search;

import com.foocmend.commons.Utils;
import com.foocmend.controllers.search.SearchHistoryForm;
import com.foocmend.entities.QSearchHistory;
import com.foocmend.entities.SearchHistory;
import com.foocmend.entities.SearchHistoryId;
import com.foocmend.repositories.SearchHistoryRepository;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static org.springframework.data.domain.Sort.Order.desc;

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

        Pageable pageable = PageRequest.of(0, limit, Sort.by(desc("updateDt"), desc("searchCnt")));
        Page<SearchHistory> data = repository.findAll(builder, pageable);
        return data.getContent();
    }


    public List<SearchHistory> getRecents() {
        return getRecents(3, 10);
    }

    public List<SearchHistory> getRecents(int days) {
        return getRecents(days,10);
    }

    public List<String> getList(SearchHistoryForm form) {
        /*
        인기 검색 : 모든 유저의 SearchHistory SearchCnt 기준으로 최대 클릭순 1~5 정렬
        최근검색 : 이름순 -> 시간대 별 정렬 \, 기록 유지 기간은 7일
        추천 검색 : 유저 SearchCnt 최다순 카테고리(테마) 1~5 정렬
        자동 완성 검색 : Like '%O%' 과 같이 한 글자 기입시 관련 단어
         */

        QSearchHistory searchHistory = QSearchHistory.searchHistory;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(searchHistory.searchDate.goe(LocalDate.now().minusDays(7)));

        String skey = form.getSkey();
        String type = form.getSearchType();
        type = Objects.requireNonNullElse(type, "popular");

        if (skey != null && !skey.isBlank()) {
            skey = skey.trim();
            builder.and(searchHistory.keyword.contains(skey));
        }

        if (type.equals("recent") || type.equals("recommend")) {
            builder.and(searchHistory.uid.eq(utils.getBrowserId()));
        }

        Sort sort = Sort.by(desc("searchCnt"));
        if (type.equals("popular") || type.equals("recent")) {
            sort = Sort.by(desc("updateDt"));
        }

        Pageable pageable = PageRequest.of(0, 10, sort);
        Page<SearchHistory> data = repository.findAll(builder, pageable);
        List<String> items = data.getContent().stream().map(SearchHistory::getKeyword).distinct().toList();
        return items;

    }
}
