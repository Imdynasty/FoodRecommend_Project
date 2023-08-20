package com.foocmend.services.member;

import com.foocmend.commons.constants.Role;
import com.foocmend.controllers.member.SignUpForm;
import com.foocmend.entities.Member;
import com.foocmend.repositories.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

import static java.util.stream.Collectors.joining;

@Service
@RequiredArgsConstructor
public class SaveMemberService {
    private final MemberRepository repository;

    private final PasswordEncoder encoder;

    public void save(SignUpForm form) {

        form.setPassword(encoder.encode(form.getPassword()));
        Member member = new ModelMapper().map(form, Member.class);

        member.setRole(Role.USER); // 기본 권한은 일반 사용자

        /** 선호 음식 문자열 변환 처리 S - 예 KOREA||JAPAN */
        String[] favoriteFoods = form.getFavoriteFoods();
        String favoriteFoodsStr = Arrays.stream(favoriteFoods).collect(joining("||"));
        member.setFavoriteFoods(favoriteFoodsStr);
        /** 선호 음식 문자열 변환 처리 E */

        repository.saveAndFlush(member);
    }
}
