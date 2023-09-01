package com.foocmend.controllers.board;

import com.foocmend.entities.FileInfo;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardForm {
    private Long id; // 게시글 번호

    @NotBlank
    private String bId;

    @NotBlank
    private String gid = UUID.randomUUID().toString();

    @NotBlank
    private String poster; // 작성자
    private String guestPw; // 비회원 비밀번호
    private String category; // 게시판 분류

    @NotBlank
    private String subject; // 제목

    @NotBlank
    private String content; // 내용

    private Long userNo; // 회원번호\

    private List<FileInfo> editorImages; // 게시판 에디터 첨부 이미지

    private List<FileInfo> attachFiles; // 게시판 첨부 파일
}
