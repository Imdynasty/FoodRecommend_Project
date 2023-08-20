package com.foocmend.services.member;

import com.foocmend.controllers.member.SignUpForm;
import com.foocmend.entities.Member;
import com.foocmend.repositories.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SaveMemberService {
    private final MemberRepository repository;

    private final PasswordEncoder encoder;

    public void save(SignUpForm form) {

        String hash = encoder.encode(form.getPassword());

        Member member = new ModelMapper().map(form, Member.class);

        repository.saveAndFlush(member);

    }

}
