package com.foocmend.entities;

import com.foocmend.commons.constants.Gender;
import com.foocmend.commons.constants.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class EntityReview extends Base{
    @Id
    @GeneratedValue
    private Long memNo;

    @Column(length=10, nullable = false)
    private String nickname;

    @Column(length=10, nullable = false)
    private String subject;

    @Column(length=10, nullable = false)
    @Lob
    private String content;

    /*
    @Id
    @Column(name="code_", length=45)
    private String code;

    @Lob
    @Column(name="value_")
    private String value;

     */
}
