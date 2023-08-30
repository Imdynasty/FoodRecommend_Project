package com.foocmend.repositories;

import com.foocmend.entities.FileInfo;
import com.foocmend.entities.QFileInfo;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

import static org.springframework.data.domain.Sort.Order.asc;

public interface FileInfoRepository extends JpaRepository<FileInfo, Long>, QuerydslPredicateExecutor<FileInfo> {

    /**
     *
     * @param gid
     * @param location
     * @param mode : all - 완료, 미완료 파일 모두 조회, done - 완료 파일, undone : 미완료 파일
     * @return
     */
    default List<FileInfo> getFiles(String gid, String location, String mode) {
        QFileInfo fileInfo = QFileInfo.fileInfo;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(fileInfo.gid.eq(gid));

        if (location != null && !location.isBlank()) {
            builder.and(fileInfo.location.eq(location));
        }

        if (mode.equals("done")) builder.and(fileInfo.done.eq(true)); // 작업 완료 파일
        else if (mode.equals("undone")) builder.and(fileInfo.done.eq(false)); // 미 완료 파일

        List<FileInfo> items = (List<FileInfo>)findAll(builder, Sort.by(asc("createdDt")));

        return items;
    }

    /**
     * 완료, 미완료 파일 모두 조회
     *
     * @param gid
     * @param location
     * @return
     */
    default List<FileInfo> getFiles(String gid, String location) {
        return getFiles(gid, location, "all");
    }

    default List<FileInfo> getFiles(String gid) {
        return getFiles(gid, null);
    }

    /**
     * 업로드 완료된 파일
     *
     * @param gid
     * @param location
     * @return
     */
    default List<FileInfo> getFilesDone(String gid, String location) {
        return getFiles(gid, location, "done");
    }

    default List<FileInfo> getFilesDone(String gid) {
        return getFilesDone(gid, null);
    }

    /**
     * 작업 완료 처리
     *
     * @param gid
     */
    default void processDone(String gid) {
        List<FileInfo> items = getFiles(gid);
        items.stream().forEach(item -> {
            item.setDone(true);
        });

        flush();
    }
}