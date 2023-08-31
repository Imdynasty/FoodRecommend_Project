package com.foocmend.controllers.restaurant;

import lombok.Data;

@Data
public class RestaurantSearchForm {
    private int page = 1;
    private int limit = 20;
    private String type;
    private String sopt;
    private String skey;
    private String sort;
}
