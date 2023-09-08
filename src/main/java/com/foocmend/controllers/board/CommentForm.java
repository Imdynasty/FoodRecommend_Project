package com.foocmend.controllers.board;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentForm {

    private Long id; // 댓글 등록 번호

    @NotBlank
    private String commenter;
    private String guestPw; // 비회원 게시글 비밀번호
    @NotBlank
    private String content;
    @NotNull
    private Long boardDataId; // 게시글 번호
}
