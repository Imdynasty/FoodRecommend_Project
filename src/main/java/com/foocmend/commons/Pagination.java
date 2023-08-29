package com.foocmend.commons;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 페이지네이션 공통
 *
 */
@Data
public class Pagination {
    private int page = 1;
    private int total;
    private int ranges = 10;
    private int limit = 20;
    private int totalPages; // 전체 페이지 갯수
    private int firstRangePage; // 구간별 시작 페이지
    private int lastRangePage; // 구간별 종료 페이지
    private int prevFirstPage; // 이전 구간 첫번째 페이지
    private int nextFirstPage; // 다음 구간 첫번째 페이지

    private String baseUrl; // 기본 페이지 URL;

    private HttpServletRequest request;

    /**
     *
     * @param page : 페이지 번호(1,2,3...)
     * @param total : 전체 레코드 갯수
     * @param ranges : 페이지 구간별 페이지 번호 갯수
     * @param limit : 1페이지당 레코드 갯수
     */
    public Pagination(int page, int total, int ranges, int limit, HttpServletRequest request) {
        page = Utils.getNumber(page, 1);
        ranges = Utils.getNumber(ranges, 1);
        limit = Utils.getNumber(limit, 1);
        total = Utils.getNumber(total, 1);

        int totalPages = (int)Math.ceil(total / (double)limit);


        int rangeCnt = (int)Math.floor((page - 1) / ranges);
        int lastRangeCnt = (int)Math.floor((totalPages - 1) / ranges);
        int firstRangePage = rangeCnt * ranges + 1;
        int lastRangePage = firstRangePage + ranges - 1;
        lastRangePage = lastRangePage > totalPages ? totalPages : lastRangePage;


        /**
         * 이전 구간 첫번째 페이지
         *      -> 첫번째 구간이 아닐때만 노출
         * 다음 구간 첫번째 페이지
         *      -> 마지막 구간이 아닐때만 노출
         */
        if (rangeCnt > 0) {
            this.prevFirstPage = (rangeCnt - 1) * ranges + 1;
        }

        if (rangeCnt < lastRangeCnt) {
            this.nextFirstPage = (rangeCnt + 1) * ranges + 1;
        }

        String qs = "";
        if (request != null && request.getQueryString() != null) {
            qs = Arrays.stream(request.getQueryString().split("&")).filter(s->!s.contains("page=")).collect(Collectors.joining("&"));
        }

        this.baseUrl = qs.isBlank() ? "?page=":"?" + qs + "&page=";

        this.page = page;
        this.total = total;
        this.ranges = ranges;
        this.limit = limit;
        this.totalPages = totalPages;
        this.firstRangePage = firstRangePage;
        this.lastRangePage = lastRangePage;
        this.request = request;
    }

    public Pagination(int page, int total, int ranges, int limit) {
        this(page, total, ranges, limit, null);
    }

    public List<String[]> getPages() {


        return IntStream.rangeClosed(firstRangePage, lastRangePage)
                .mapToObj(p -> new String[] { String.valueOf(p), baseUrl + p})
                .toList();
    }
}
