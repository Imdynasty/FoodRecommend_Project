package com.foocmend.commons.validators;

import com.foocmend.commons.MemberUtil;
import com.foocmend.controllers.member.SignUpForm;
import com.foocmend.repositories.MemberRepository;
import com.foocmend.services.member.DetailMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class EditInfoValidator implements Validator, PasswordValidator, MobileValidator{
    private final MemberRepository repository;
    private final MemberUtil memberUtil;


    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(SignUpForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SignUpForm form = (SignUpForm)target;
        DetailMember member = memberUtil.getMember();

        /**
         * 1. 비밀번호 복잡성 - 숫자, 영어
         * 2. 비밀번호 확인
         * 3. 닉네임 중복
         * 4. 휴대전화번호 형식 체크
         */
        String password = form.getPassword();
        String passwordRe = form.getPasswordRe();
        String mobile = form.getMobile();
        String nickName = form.getNickname();


        // 1. 비밀번호 복잡성 - 숫자, 영어
        if (password != null && !password.isEmpty() &&
                (!alphaCheck(password, false) || !numberCheck(password))) {
            errors.rejectValue("password", "complexity");
        }

        // 2. 비밀번호 확인
        if (password != null && !password.isEmpty() && passwordRe != null && !passwordRe.isEmpty() && !password.equals(passwordRe)) {
            errors.rejectValue("passwordRe", "mismatch");
        }

        // 3. 닉네임 중복
        if (!member.getNickname().equals(nickName) && nickName != null && !nickName.isBlank() && repository.existsNickname(nickName)) {
            errors.rejectValue("nickname", "duplicate");
        }

        // 4. 휴대전화번호 형식 체크
        if (mobile != null && !mobile.isBlank()) {
            mobile = mobile.replaceAll("\\D", "");
            form.setMobile(mobile);
            if (!mobileNumCheck(mobile)) {
                errors.rejectValue("mobile", "format");
            }
        }
    }
}
