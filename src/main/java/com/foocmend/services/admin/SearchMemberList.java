package com.foocmend.services.admin;

import com.foocmend.commons.ListData;
import com.foocmend.commons.Pagination;
import com.foocmend.commons.Utils;
import com.foocmend.commons.constants.Role;
import com.foocmend.entities.BoardData;
import com.foocmend.entities.Member;
import com.foocmend.entities.QBoardData;
import com.foocmend.entities.QMember;
import com.foocmend.repositories.BoardDataRepository;
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
    private final BoardDataRepository boardDataRepository;

    public ListData<Member> getList(MemberSearch search) {
        QMember member = QMember.member;

        int limit = search.getLimit();
        limit = Utils.getNumber(limit, 20);
        int page = search.getPage();
        page = Utils.getNumber(page, 1);


        /** 검색 처리 S*/
        BooleanBuilder andBuilder = new BooleanBuilder();
        String soption = search.getSoption();
        String searchkey = search.getSearchkey();
        List<Role> roles = search.getRoles();

        if (soption != null && !soption.isBlank() && searchkey != null && !searchkey.isBlank()) {
            searchkey = searchkey.trim();
            if (soption.equals("all")) { // 통합검색
                BooleanBuilder orBuilder = new BooleanBuilder();
                orBuilder.or(member.email.contains(searchkey))
                        .or(member.nickname.contains(searchkey))
                        .or(member.address.contains(searchkey))
                        .or(member.favoriteFoods.contains(searchkey));
                andBuilder.and(orBuilder);
            } else if (soption.equals("email")) {
                andBuilder.and(member.email.contains(searchkey));
            } else if (soption.equals("nickname")) {
                andBuilder.and(member.nickname.contains(searchkey));
            } else if (soption.equals("address")) {
                andBuilder.and(member.address.contains(searchkey));
            } else if (soption.equals("favoriteFoods")) {
                andBuilder.and(member.favoriteFoods.contains(searchkey));
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
