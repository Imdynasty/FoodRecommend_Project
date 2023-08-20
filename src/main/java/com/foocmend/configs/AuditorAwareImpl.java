package com.foocmend.configs;

import com.foocmend.services.member.DetailMember;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuditorAwareImpl implements AuditorAware<Long> {
    @Override
    public Optional<Long> getCurrentAuditor() {
        Long memNo = null;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserDetails) {
            DetailMember detailMember = (DetailMember) auth.getPrincipal();
            memNo = detailMember.getMemNo();
        }

        return Optional.ofNullable(memNo);
    }
}
