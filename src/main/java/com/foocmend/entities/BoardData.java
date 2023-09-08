package com.foocmend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(indexes={
        @Index(name="idx_boarddata_category", columnList = "category DESC"),
        @Index(name="idx_boarddata_createDt", columnList = "createdDt DESC")
})
public class BoardData extends Base {
    @Id @GeneratedValue
    private Long id; // 게시글 번호
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="bId")
    private Board board;

    @Column(length=65, nullable = false)
    private String gid = UUID.randomUUID().toString();

    @Column(length=40, nullable = false)
    private String poster; // 작성자

    @Column(length=65)
    private String guestPw; // 비회원 비밀번호

    @Column(length=60)
    private String category; // 게시판 분류

    @Column(nullable = false)
    private String subject; // 제목

    @Lob
    @Column(nullable = false)
    private String content; // 내용
    private int hit; // 조회수

    private String ua; // User-Agent : 브라우저 정보

    @Column(length=20)
    private String ip; // 작성자 IP

    private int commentCnt; // 댓글 수

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="memNo")
    private Member member; // 작성 회원

    // 댓글 목록
    @OneToMany(mappedBy = "boardData", fetch=FetchType.LAZY)
    private List<BoardComment> comments = new ArrayList<>();

    @Transient
    private List<FileInfo> editorImages; // 게시판 에디터 첨부 이미지

    @Transient
    private List<FileInfo> attachFiles; // 게시판 첨부 파일
}
