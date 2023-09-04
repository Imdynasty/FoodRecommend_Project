package com.foocmend.controllers.member;

import com.foocmend.commons.MemberUtil;
import com.foocmend.entities.Member;
import com.foocmend.repositories.MemberRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member/withdraw")
public class Withdraw {

    private final MemberRepository repository;
    private final MemberUtil memberUtil;
    private final HttpSession session;

    @GetMapping
    public String withdraw() {


        return "front/member/withdraw";
    }

    @PostMapping
    public String withdrawPs(@RequestParam String email, @RequestParam String password, Model model, Error error) {

        if(!memberUtil.isLogin()) {
            return "redirect:/member/login";
        }

        Member currMember = memberUtil.getEntity();
        Member member = repository.findByEmail(email);

        if(currMember != null && currMember.getEmail().equals(email)&& passwordMatches(currMember.getPassword(), password)) {

            repository.delete(member);
            repository.flush();
            session.invalidate();

            model.addAttribute("withdrawSuccess","회원탈퇴가 완료 되었습니다.");

            return "redirect:/";
        } else {
            model.addAttribute("withdrawFailed","아이디와 비밀번호를 확인해주세요.");
            return "front/member/withdrawResult";
        }
    }


    private boolean passwordMatches(String encodedPassword, String password) {

         PasswordEncoder encoder = new BCryptPasswordEncoder();
         boolean matches = encoder.matches(password, encodedPassword);
        return matches;
    }


}
