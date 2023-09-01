package com.foocmend.controllers.member;

import com.foocmend.commons.Utils;
import com.foocmend.commons.constants.Foods;
import com.foocmend.commons.validators.SignUpValidator;
import com.foocmend.services.member.SaveMemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 회원가입
 *
 */
@Controller
@RequestMapping("/member/signup")
@RequiredArgsConstructor
public class SignUp {

    private final SaveMemberService saveMemberService;
    private final SignUpValidator signUpValidator;
    private final Utils utils;

    @GetMapping
    public String signup(@ModelAttribute SignUpForm signUpForm, Model model) {
        commonProcess(model);
        return utils.view("member/signup");
    }

    @PostMapping
    public String signupPs(@Valid SignUpForm signUpForm, Errors errors, Model model) {
        commonProcess(model);

        signUpValidator.validate(signUpForm, errors);

        if (errors.hasErrors()) {
            return utils.view("member/signup");
        }

        saveMemberService.save(signUpForm);


        return "redirect:/member/login";
    }

    /**
     * 회원가입 공통 처리
     * @param model
     */
    private void commonProcess(Model model) {
        model.addAttribute("foods", Foods.getList());
        model.addAttribute("addCss", new String[] { "member/style"} );
        model.addAttribute("addCommonScript", new String[] {"address"});
    }
}
