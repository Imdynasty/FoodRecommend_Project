package com.foocmend.commons.validators;

import com.foocmend.controllers.member.SignUpForm;
import com.foocmend.repositories.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class SignUpValidator implements Validator, PasswordValidator, MobileValidator {
    private final MemberRepository repository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(SignUpForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SignUpForm form = (SignUpForm)target;

        /**
         * 1. 이메일 중복 여부
         * 2. 비밀번호 복잡성 - 숫자, 영어
         * 3. 비밀번호 확인
         * 4. 닉네임 중복
         * 5. 휴대전화번호 형식 체크
         */
        String email = form.getEmail();
        String password = form.getPassword();
        String passwordRe = form.getPasswordRe();
        String mobile = form.getMobile();
        String nickName = form.getNickname();

        // 1. 이메일 중복 여부
        if (email != null && !email.isBlank() && repository.exists(email)) {
            errors.rejectValue("email", "duplicate");
        }

        // 2. 비밀번호 복잡성 - 숫자, 영어
        if (password != null && !password.isEmpty() &&
                (!alphaCheck(password, false) || !numberCheck(password))) {
            errors.rejectValue("password", "complexity");
        }

        // 3. 비밀번호 확인
        if (password != null && !password.isEmpty() && passwordRe != null && !passwordRe.isEmpty() && !password.equals(passwordRe)) {
            errors.rejectValue("passwordRe", "mismatch");
        }

        // 4. 닉네임 중복
        if (nickName != null && !nickName.isBlank() && repository.existsNickname(nickName)) {
            errors.rejectValue("nickname", "duplicate");
        }

        // 5. 휴대전화번호 형식 체크
        if (mobile != null && !mobile.isBlank()) {
            mobile = mobile.replaceAll("\\D", "");
            form.setMobile(mobile);
            if (!mobileNumCheck(mobile)) {
                errors.rejectValue("mobile", "format");
            }
        }
    }
}
