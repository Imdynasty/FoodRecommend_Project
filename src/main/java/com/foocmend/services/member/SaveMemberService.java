package com.foocmend.services.member;

import com.foocmend.commons.MemberUtil;
import com.foocmend.commons.constants.Gender;
import com.foocmend.commons.constants.Role;
import com.foocmend.controllers.member.SignUpForm;
import com.foocmend.entities.Member;
import com.foocmend.repositories.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

import static java.util.stream.Collectors.joining;

@Service
@RequiredArgsConstructor
public class SaveMemberService {
    private final MemberRepository repository;

    private final PasswordEncoder encoder;

    private final MemberUtil memberUtil;

    public void save(SignUpForm form) {
        Member member = null;
        if (memberUtil.isLogin() && form.getMode().equals("edit")) { // 회원정보 수정
            member = memberUtil.getEntity();
        } else { // 가입
            member = new Member();
            member.setRole(Role.USER); // 기본 권한은 일반 사용자
        }

        member.setEmail(form.getEmail());
        member.setNickname(form.getNickname());
        member.setGender(Gender.valueOf(form.getGender()));
        member.setZipcode(form.getZipcode());
        member.setAddress(form.getAddress());
        member.setBirthDate(form.getBirthDate());
        member.setMobile(form.getMobile());

        /** 비밀번호 해시화 처리 */
        String password = form.getPassword();
        if (password != null && !password.isBlank()) {
            member.setPassword(encoder.encode(form.getPassword()));
        }

        /** 선호 음식 문자열 변환 처리 S - 예 KOREA||JAPAN */
        String[] favoriteFoods = form.getFavoriteFoods();
        if (favoriteFoods != null) {
            String favoriteFoodsStr = Arrays.stream(favoriteFoods).collect(joining("||"));
            member.setFavoriteFoods(favoriteFoodsStr);
        }
        /** 선호 음식 문자열 변환 처리 E */
        repository.saveAndFlush(member);
    }
}