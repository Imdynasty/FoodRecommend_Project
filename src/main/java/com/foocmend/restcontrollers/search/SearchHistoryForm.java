package com.foocmend.restcontrollers.search;

import lombok.Data;

@Data
public class SearchHistoryForm {
    private String searchType = "popular";
    private String sopt;
    private String skey;
    private String sido;
    private String sigugun;
}
