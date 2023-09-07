package com.foocmend.entities;

import com.foocmend.commons.constants.Gender;
import com.foocmend.commons.constants.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data @Builder
@AllArgsConstructor @NoArgsConstructor
@Table(indexes = {
        @Index(name="idx_member_gender", columnList = "gender"),
        @Index(name="idx_member_birthDate", columnList = "birthDate")
})
public class Member extends Base {
    @Id @GeneratedValue
    private Long memNo;

    @Column(length=80, nullable = false, unique = true)
    private String email;

    @Column(length=65, nullable = false)
    private String password;

    @Column(length=10, nullable = false, unique = true)
    private String nickname;

    @Column(length=11)
    private String mobile;
    @Column(length=10, nullable = false)
    private String zipcode;
    @Column(length=100, nullable = false)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(length=10, nullable = false)
    private Gender gender;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(length=200, nullable = false)
    private String favoriteFoods;

    @Enumerated(EnumType.STRING)
    @Column(length=10, nullable = false)
    private Role role = Role.USER;

}
