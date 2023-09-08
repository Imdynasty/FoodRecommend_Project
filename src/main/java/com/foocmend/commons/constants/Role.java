package com.foocmend.commons.constants;

import com.foocmend.commons.Utils;

import java.util.Arrays;
import java.util.List;

public enum Role {
    ALL,
    ADMIN,
    USER;

    public String getString() {
        return Utils.getMessage("MemberStatus." + name(), "common");
    }

    public static List<String[]> getList() {
        return Arrays.asList(
                new String[]{ADMIN.name(),ADMIN.getString()},
                new String[]{USER.name(),USER.getString()}
        );
    }

}
