package com.foocmend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Data @Builder
@NoArgsConstructor
@AllArgsConstructor
public class SiteVisit {

    @Id
    private Long number;

    private LocalDateTime visitDate;

}
