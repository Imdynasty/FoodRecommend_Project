package com.foocmend.commons;

import com.foocmend.commons.constants.Role;
import com.foocmend.services.member.DetailMember;
import com.foocmend.entities.Member;
import jakarta.servlet.http.HttpSession;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MemberUtil {

    @Autowired
    private HttpSession session;

    /**
     * 로그인 여부
     * @return
     */
    public boolean isLogin() {

        return getMember() != null;
    }

    /**
     * 관리자 여부
     * @return
     */
    public boolean isAdmin() {

        return isLogin() && getMember().getRole() == Role.ADMIN;
    }

    public DetailMember getMember() {

        DetailMember detailMember = (DetailMember)session.getAttribute("detailMember");

        return detailMember;
    }

    public Member getEntity() {

        if (isLogin()) {
            Member member = new ModelMapper().map(getMember(), Member.class);
            return member;
        }

        return null;
    }
}