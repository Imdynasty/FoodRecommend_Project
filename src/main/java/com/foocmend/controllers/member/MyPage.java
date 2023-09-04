package com.foocmend.controllers.member;

import com.foocmend.commons.MemberUtil;
import com.foocmend.commons.Utils;
import com.foocmend.commons.constants.Foods;
import com.foocmend.commons.validators.EditInfoValidator;
import com.foocmend.commons.validators.SignUpValidator;
import com.foocmend.entities.Member;
import com.foocmend.repositories.MemberRepository;
import com.foocmend.services.member.DeleteMemberService;
import com.foocmend.services.member.SaveMemberService;
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
    private final DeleteMemberService deleteService;

    @GetMapping
    public String myPageView() {

        return "front/member/mypage";
    }

    @GetMapping("/delete")
    public String delete(Model model) {


        return "front/member/deleteform";
    }

    @PostMapping("/delete")
    public String deletePs(@RequestParam String password) {
        Member member = memberUtil.getEntity();

        if(!memberUtil.isLogin()) {
            return "redirect:/member/login";
        } else {
            deleteService.delete(password);
            return "/";
        }
    }


    @GetMapping("/edit")
    public String editInfo(@ModelAttribute SignUpForm signUpForm, Model model) {
        commonProcess(model);
        Member member = memberUtil.getEntity();

        if(member != null) {
            signUpForm.setEmail(member.getEmail());
            signUpForm.setNickname(member.getNickname());
            signUpForm.setGender(member.getGender().toString());
            signUpForm.setZipcode(member.getZipcode());
            signUpForm.setAddress(member.getAddress());
            signUpForm.setBirthDate(member.getBirthDate());
            signUpForm.setMobile(member.getMobile());

            signUpForm.setMode("edit");
        }


//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if (auth != null && auth.isAuthenticated()) {
//            Object principal = auth.getPrincipal();
//            if (principal instanceof UserDetails) {
//                UserDetails userDetails = (UserDetails) principal;
//                String Email = userDetails.getUsername();
//
//                Member loggedInMember = repository.findByEmail(Email);

//                signUpForm.setEmail(loggedInMember.getEmail());
//                signUpForm.setNickname(loggedInMember.getNickname());
//                signUpForm.setGender(loggedInMember.getGender().toString());
//                signUpForm.setZipcode(loggedInMember.getZipcode());
//                signUpForm.setAddress(loggedInMember.getAddress());
//                signUpForm.setBirthDate(loggedInMember.getBirthDate());
//                signUpForm.setMobile(loggedInMember.getMobile());

//                signUpForm.setMode("edit");
//            }
//        }
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
