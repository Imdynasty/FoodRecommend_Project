package com.foocmend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(indexes={
        @Index(name="idx_fileinfo_gid", columnList = "gid"),
        @Index(name="idx_fileinfo_gid_location", columnList = "gid,location")
})
public class FileInfo extends BaseMember {
    @Id
    @GeneratedValue
    private Long id;
    @Column(length=45, nullable = false)
    private String gid = UUID.randomUUID().toString();
    @Column(length=45)
    private String location;

    @Column(length=100, nullable = false)
    private String fileName;

    @Column(length=45)
    private String extension;

    @Column(length=65)
    private String fileType;

    private boolean done; // 작업 완료 여부

    @Transient
    private String filePath; // 실 서버 업로드 경로

    @Transient
    private String fileUrl; // 서버 접속 URL

    @Transient
    private String[] thumbsPath; // 썸네일 이미지 경로

    @Transient
    private String[] thumbsUrl; // 썸네일 이미지 접속 URL
}