package com.foocmend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity @Data @Builder
@NoArgsConstructor @AllArgsConstructor
@IdClass(SearchHistoryId.class)
public class SearchHistory {
    @Id
    @Column(name="_uid")
    private int uid;

    @Id
    @Column(length=40, nullable = false)
    private String keyword;

    /** 검색일 */
    @Id
    private LocalDate searchDate;

    /** 검색 빈도 수 */
    private int searchCnt;
}
