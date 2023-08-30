package com.foocmend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder @NoArgsConstructor @AllArgsConstructor
public class Category {
    @Id
    @Column(length=40)
    private String cateCd;
    @Column(length=60, nullable = false)
    private String cateNm;
}
