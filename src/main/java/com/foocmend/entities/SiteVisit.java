package com.foocmend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data @Builder
@NoArgsConstructor
@AllArgsConstructor
public class SiteVisit {

    @Column(nullable = false)
    private Long number;

    @Id
    @Column(nullable = false)
    private LocalDateTime visitDate;

}
