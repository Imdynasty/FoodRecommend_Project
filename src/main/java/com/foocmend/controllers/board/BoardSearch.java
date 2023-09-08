package com.foocmend.controllers.board;

import lombok.Data;

@Data
public class BoardSearch {
    private int page = 1;
    private int limit = 20;
    private String sopt;
    private String skey;
    private String sort;
    private String email;

    private Long extraLong1;
    private Long extraLong2;
    private String extraText1;
    private String extraText2;
}
