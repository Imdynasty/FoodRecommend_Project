package com.foocmend.services.admin;

import com.foocmend.commons.ListData;
import com.foocmend.commons.Pagination;
import com.foocmend.commons.Utils;
import com.foocmend.commons.constants.Role;
import com.foocmend.entities.Member;
import com.foocmend.entities.QMember;
import com.foocmend.repositories.MemberRepository;
import com.querydsl.core.BooleanBuilder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.data.domain.Sort.Order.desc;

@Service
@RequiredArgsConstructor
public class SearchMemberList {

    private final MemberRepository memberRepository;
    private final HttpServletRequest request;

    public ListData<Member> getList(MemberSearch search) {
        QMember member = QMember.member;

        int limit = search.getLimit();
        limit = Utils.getNumber(limit, 20);
        int page = search.getPage();
        page = Utils.getNumber(page, 1);

        /** 검색 처리 S*/
        BooleanBuilder andBuilder = new BooleanBuilder();
        String sopt = search.getSopt();
        String skey = search.getSkey();
        List<Role> roles = search.getRoles();
        if (sopt != null && !sopt.isBlank() && skey != null && !skey.isBlank()) {
            skey = skey.trim();
            if (sopt.equals("all")) { // 통합검색
                BooleanBuilder orBuilder = new BooleanBuilder();
                orBuilder.or(member.email.contains(skey))
                        .or(member.nickname.contains(skey))
                        .or(member.address.contains(skey))
                        .or(member.favoriteFoods.contains(skey));
                andBuilder.and(orBuilder);
            } else if (sopt.equals("email")) {
                andBuilder.and(member.email.contains(skey));
            } else if (sopt.equals("nickname")) {
                andBuilder.and(member.nickname.contains(skey));
            } else if (sopt.equals("address")) {
                andBuilder.and(member.address.contains(skey));
            } else if (sopt.equals("favoriteFoods")) {
                andBuilder.and(member.favoriteFoods.contains(skey));
            }
        }

        /** 회원구분 검색 처리 */
        if (roles != null && !roles.isEmpty()) {
            andBuilder.and(member.role.in(roles));
        }
        /** 검색 처리 E */

        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(desc("createdDt")));
        Page<Member> pData = memberRepository.findAll(andBuilder, pageable);

        ListData<Member> data = new ListData<>();
        data.setContent(pData.getContent());

        int total = (int)pData.getTotalElements();
        Pagination pagination = new Pagination(page, total, 10, limit, request);
        data.setPagination(pagination);


        return data;
    }

}
