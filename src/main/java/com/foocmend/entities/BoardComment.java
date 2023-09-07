package com.foocmend.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@AllArgsConstructor @NoArgsConstructor
public class BoardComment extends Base {
    @Id @GeneratedValue
    private Long id;
    private String commenter;
    private String guestPw;
    private String content;
    private String ua; // User-Agent : 브라우저 정보

    @Column(length=20)
    private String ip; // 작성자 IP

    @ToString.Exclude
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="boardDataId")
    private BoardData boardData;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="memNo")
    private Member member;
}