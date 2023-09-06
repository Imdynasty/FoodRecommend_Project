package com.foocmend.commons.constants;

import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public enum Foods {
    KOREA,
    AMERICA,
    JAPAN,
    CHINESE,
    SNACK, // 분식
    CAFE,
    FAMILY_RESTAURANT,
    SASHIMI, // 횟집
    SOUPS, // 탕류
    FAST_FOOD,
    BUFFET,
    PUB, // 술집
    ETC;

    public static List<String[]> getList() {
        ResourceBundle bundle = ResourceBundle.getBundle("messages.commons");

        return Arrays.asList(
                new String[] { "KOREA", bundle.getString("food.KOREA")},
                new String[] { "AMERICA", bundle.getString("food.AMERICA")},
                new String[] { "JAPAN", bundle.getString("food.JAPAN")},
                new String[] { "CHINESE", bundle.getString("food.CHINESE")},
                new String[] { "SNACK", bundle.getString("food.SNACK")},
                new String[] { "CAFE", bundle.getString("food.CAFE")},
                new String[] { "FAMILY_RESTAURANT", bundle.getString("food.FAMILY_RESTAURANT")},
                new String[] { "SASHIMI", bundle.getString("food.SASHIMI")},
                new String[] { "SOUPS", bundle.getString("food.SOUPS")},
                new String[] { "FAST_FOOD", bundle.getString("food.FAST_FOOD")},
                new String[] { "BUFFET", bundle.getString("food.BUFFET")},
                new String[] { "PUB", bundle.getString("food.PUB")},
                new String[] { "ETC", bundle.getString("food.ETC")}
        );
    }
}
