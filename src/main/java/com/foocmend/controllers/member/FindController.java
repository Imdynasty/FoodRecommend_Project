package com.foocmend.controllers.member;

import com.foocmend.services.member.SearchMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/member/find")
@RequiredArgsConstructor
public class FindController {

    private final SearchMemberService searchMemberService;

    @GetMapping("/email")
    public String findIdForm() {
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
    public String resetPassword(Model model, @RequestParam String email, @RequestParam String currPassword, @RequestParam String newPassword) {
        boolean success = searchMemberService.resetPasswordByEmailAndNickname(email, currPassword, newPassword);

        if(success) {
            model.addAttribute("msg", "비밀번호 설정이 완료되었습니다");
        } else {
            model.addAttribute("error", "이메일과 닉네임이 일치하지 않습니다");
        }
        return "front/member/resetPasswordResult";
    }
}
