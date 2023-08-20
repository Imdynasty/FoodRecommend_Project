package com.foocmend.commons.constants;

import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public enum Foods {
    KOREA,
    AMERICA,
    JAPAN;

    public static List<String[]> getList() {
        ResourceBundle bundle = ResourceBundle.getBundle("messages.commons");

        return Arrays.asList(
                new String[] { "KOREA", bundle.getString("food.KOREA")},
                new String[] { "AMERICA", bundle.getString("food.AMERICA")},
                new String[] { "JAPAN", bundle.getString("food.JAPAN")}
        );
    }
}
