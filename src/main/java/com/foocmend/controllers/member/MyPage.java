package com.foocmend.controllers.member;

import com.foocmend.commons.constants.Foods;
import com.foocmend.commons.validators.EditInfoValidator;
import com.foocmend.entities.Member;
import com.foocmend.repositories.MemberRepository;
import com.foocmend.services.member.SaveMemberService;
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

    @GetMapping
    public String myPageView(Model model) {
        commonProcess(model);

        return "front/member/mypage";
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

                signUpForm.setMode("edit");
                signUpForm.setEmail(loggedInMember.getEmail());
                signUpForm.setNickname(loggedInMember.getNickname());
                signUpForm.setGender(loggedInMember.getGender().toString());
                signUpForm.setZipcode(loggedInMember.getZipcode());
                signUpForm.setAddress(loggedInMember.getAddress());
                signUpForm.setBirthDate(loggedInMember.getBirthDate());
                signUpForm.setMobile(loggedInMember.getMobile());
            }
        }
        return "front/member/editInfo";
    }


    @PostMapping("/edit")
    public String editInfoPs(SignUpForm signUpForm, Errors errors, Model model) {
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
        model.addAttribute("addCss", new String[] { "member/style"});
        model.addAttribute("addCommonScript", new String[] {"address"});
    }

}
