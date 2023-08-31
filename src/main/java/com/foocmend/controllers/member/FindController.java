package com.foocmend.controllers.member;

import com.foocmend.commons.validators.EditInfoValidator;
import com.foocmend.entities.Member;
import com.foocmend.repositories.MemberRepository;
import com.foocmend.services.member.SearchMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Random;

@Controller
@RequestMapping("/member/find")
@RequiredArgsConstructor
public class FindController {

    private final SearchMemberService searchMemberService;
    private final EditInfoValidator editInfoValidator;
    private final MemberRepository repository;
    private final PasswordEncoder encoder;

    @GetMapping("/email")
    public String findEmailForm() {
        return "front/member/findEmailForm";
    }

    @PostMapping("/email")
    public String findUserEmail(Model model, @RequestParam String email, @RequestParam String nickname) {
        String findEmail = searchMemberService.findUserIdByEmailAndNickname(email, nickname);

        if(findEmail != null) {
            model.addAttribute("findEmail", findEmail);
        } else {
            model.addAttribute("error", new UsernameNotFoundException(email));
        }

        return "front/member/findEmailResult";
    }

    @GetMapping("/password")
    public String resetPasswordForm() {

        return "front/member/resetPasswordForm";
    }


    @PostMapping("/password")
    public String resetPassword(
            @RequestParam String email,
            @RequestParam String nickname,
            @RequestParam String mobile,
            Model model) {
        Member member = repository.findByEmail(email);
        if(member.getEmail().equals(email) &&
        member.getNickname().equals(nickname) &&
        member.getMobile().equals(mobile)) {
            String newPw = generatePw();

            member.setPassword(encoder.encode(newPw));
            repository.saveAndFlush(member);
            model.addAttribute("findpw", newPw);
        } else {
            model.addAttribute("error", "일치하는 정보를 찾을 수 없습니다");
        }


        return "front/member/resetPasswordResult";
    }

    public String generatePw() {
        Random rnd = new Random();
        StringBuffer sb = new StringBuffer();

        for(int i = 0; i <=20; i++) {
            if(rnd.nextBoolean()) {
                sb.append((char)((int)(rnd.nextInt(26))+97));
            } else {
                sb.append((rnd.nextInt(10)));
            }
        }
        return sb.toString();
    }

}
