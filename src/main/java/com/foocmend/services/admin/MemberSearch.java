package com.foocmend.services.admin;

import com.foocmend.commons.constants.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberSearch {

    private int page = 1; // 페이지 번호
    private int limit = 20; // 1페이지당 레코드 갯수

    private List<Role> roles;

    private String sopt;    // 검색 옵션
    private String skey;    // 검색 키워드

}
