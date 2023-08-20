package com.foocmend.controllers.member;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class SignUpForm {
    private String mode;

    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Size(min=8)
    private String password;
    @NotBlank
    private String passwordRe;
    @NotBlank
    @Size(min=2, max=8)
    private String nickname;
    private String mobile;
    @NotBlank
    private String zipcode;
    @NotBlank
    private String address;

    @NotBlank
    private String gender;

    @NotNull
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate birthDate;

    private String[] favoriteFoods;

    @AssertTrue
    private boolean agree;
}
