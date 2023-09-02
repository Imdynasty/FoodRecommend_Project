package com.foocmend.controllers.board;

import lombok.Data;

@Data
public class BoardSearch {
    private int page = 1;
    private String sopt;
    private String skey;
    private String sort;
}
