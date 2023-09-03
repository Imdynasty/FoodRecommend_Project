package com.foocmend.entities;


import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class SearchHistoryId {
    private int uid;
    private String keyword;
    private LocalDate searchDate;

}
