package com.foocmend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name="restaurants",
    indexes = {
        @Index(name="idx_restaurant_wishcnt", columnList = "wishCnt DESC, createdDt DESC")
    }
)
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Restaurant extends BaseMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length=45)
    private String gid = UUID.randomUUID().toString();
    @Column(length=12)
    private String zipcode;
    @Column(length=100)
    private String address;
    @Column(length=100)
    private String roadAddress;
    @Column(length=10)
    private String zonecode;
    @Column(length=100)
    private String storeName;

    @Column(length=100)
    private String homepage;
    @Column(length=40)
    private String type;
    @Column(length=40)
    private String xpos;
    @Column(length=40)
    private String ypos;

    private int wishCnt;

    @Lob
    private String description;

    @JsonIgnore
    @Transient
    private List<FileInfo> mainImages; // 메인 페이지 이미지

    @JsonIgnore
    @Transient
    private List<FileInfo> listImages; // 목록 페이지 이미지

    @JsonIgnore
    @Transient
    private List<FileInfo> editorImages; // 에디터 이미지

}
