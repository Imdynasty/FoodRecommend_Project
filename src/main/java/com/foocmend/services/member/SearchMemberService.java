package com.foocmend.services.member;

import com.foocmend.entities.Member;
import com.foocmend.repositories.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchMemberService implements UserDetailsService {
    private final MemberRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = repository.findByEmail(username);
        if (member == null) {
            throw new UsernameNotFoundException(username);
        }

        List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority(member.getRole().name()));

        return DetailMember.builder()
                .memNo(member.getMemNo())
                .email(member.getEmail())
                .password(member.getPassword())
                .gender(member.getGender())
                .nickname(member.getNickname())
                .authorities(authorities)
                .build();
    }
}