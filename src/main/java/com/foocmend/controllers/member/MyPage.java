package com.foocmend.controllers.member;

import com.foocmend.commons.MemberUtil;
import com.foocmend.commons.Utils;
import com.foocmend.commons.constants.Foods;
import com.foocmend.commons.validators.EditInfoValidator;
import com.foocmend.commons.validators.SignUpValidator;
import com.foocmend.entities.Member;
import com.foocmend.repositories.MemberRepository;
import com.foocmend.services.member.DetailMember;
import com.foocmend.services.member.SaveMemberService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.*;

@Controller
@RequestMapping("/member/mypage")
@RequiredArgsConstructor
public class MyPage {

    private final MemberRepository repository;
    private final SaveMemberService saveMemberService;
    private final EditInfoValidator editInfoValidator;
    private final MemberUtil memberUtil;
    private final HttpSession session;

    @GetMapping
    public String myPageView() {

        return "front/member/mypage";
    }

    @GetMapping("/withdraw")
    public String withdraw() {

        return "front/member/withdraw";
    }

    @PostMapping("/withdraw")
    public String withdrawPs(@RequestParam String email, @RequestParam String password) {


        if (!memberUtil.isLogin()) {
            return "redirect:/member/login";
        }

        Member currMember = memberUtil.getEntity();


        if (currMember != null && currMember.getEmail().equals(email) && currMember.getPassword().equals(password)) {
            Member member = repository.findByEmail(email);

            if (member != null) {
                repository.delete(member);
                repository.flush();
                session.invalidate();
                return "redirect:/";
            }
        }
        return "redirect:/member/mypage/withdraw";
//        if(!memberUtil.isLogin()) {
//            return "redirect:/member/login";
//        }
//
//        Member currMember = memberUtil.getEntity();
//
//        if(currMember != null && currMember.getEmail().equals(email) && currMember.getPassword().equals(password)) {
//            Member member = repository.findByEmail(email);
//
//            repository.deleteById(email);
//            repository.flush();
//
//            session.invalidate();
//            return "redirect:/";
//        } else {
//            return "redirect:/member/mypage/withdraw";
//        }
    }


    @GetMapping("/edit")
    public String editInfo(@ModelAttribute SignUpForm signUpForm, Model model) {
        commonProcess(model);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            Object principal = auth.getPrincipal();
            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                String Email = userDetails.getUsername();

                Member loggedInMember = repository.findByEmail(Email);

                signUpForm.setEmail(loggedInMember.getEmail());
                signUpForm.setNickname(loggedInMember.getNickname());
                signUpForm.setGender(loggedInMember.getGender().toString());
                signUpForm.setZipcode(loggedInMember.getZipcode());
                signUpForm.setAddress(loggedInMember.getAddress());
                signUpForm.setBirthDate(loggedInMember.getBirthDate());
                signUpForm.setMobile(loggedInMember.getMobile());

                signUpForm.setMode("edit");
            }
        }
        return "front/member/editInfo";
    }


    @PostMapping("/edit")
    public String editInfoPs(@Valid SignUpForm signUpForm, Errors errors, Model model) {
        commonProcess(model);

        editInfoValidator.validate(signUpForm, errors);

        if(errors.hasErrors()) {
            return "front/member/editInfo";
        }
        signUpForm.setMode("edit");
        saveMemberService.save(signUpForm);

        return "redirect:/member/mypage";
    }


    private void commonProcess(Model model) {
        model.addAttribute("foods", Foods.getList());
        model.addAttribute("addCommonScript", new String[] {"address"});
    }

}
