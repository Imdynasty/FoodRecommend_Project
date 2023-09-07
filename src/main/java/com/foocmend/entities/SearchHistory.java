package com.foocmend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity @Data @Builder
@NoArgsConstructor @AllArgsConstructor
@IdClass(SearchHistoryId.class)
@EntityListeners(AuditingEntityListener.class)
@Table(indexes = @Index(name="idx_searchhistory_updateDt", columnList = "updateDt DESC"))
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

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime updateDt;
}
