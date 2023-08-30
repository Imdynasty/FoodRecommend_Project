package com.foocmend.controllers.admin;

import com.foocmend.entities.FileInfo;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class RestaurantForm {

    private Long id;
    private String mode; // update이면 수정
    
    @NotBlank
    private String gid = UUID.randomUUID().toString();
    private String zipcode;
    private String address;
    private String roadAddress;
    private String zonecode;
    @NotBlank(message="상호명을 입력하세요.")
    private String storeName;
    private String type;
    private String xpos;
    private String ypos;
    private String description;


    private List<FileInfo> mainImages; // 메인 페이지 이미지


    private List<FileInfo> listImages; // 목록 페이지 이미지

    private List<FileInfo> editorImages; // 에디터 이미지
}
