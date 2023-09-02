package com.foocmend.controllers.restaurant;

import lombok.Data;

import java.util.List;

@Data
public class RestaurantSearchForm {
    private int page = 1;
    private int limit = 20;
    private String type;
    private String sopt = "all";
    private String skey;
    private String sort;
    private List<String> types;

    private String sido;
    private String sigugun;
    private String[] keywords;
}
