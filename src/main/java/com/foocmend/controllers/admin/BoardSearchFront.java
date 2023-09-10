package com.foocmend.controllers.admin;

import com.foocmend.controllers.board.BoardSearch;
import lombok.Data;


@Data
public class BoardSearchFront extends BoardSearch {
    private String bId;
}
