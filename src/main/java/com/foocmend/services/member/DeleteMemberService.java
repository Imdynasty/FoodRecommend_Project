package com.foocmend.services.member;

import com.foocmend.commons.MemberUtil;
import com.foocmend.entities.Member;
import com.foocmend.repositories.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteMemberService {

    private final MemberRepository repository;
    private final MemberUtil memberUtil;


    public void delete(String password) {
        Member member = memberUtil.getEntity();
        if(!member.getPassword().equals(password)) {
            throw new RuntimeException();
        }
        repository.delete(member);
    }
}
