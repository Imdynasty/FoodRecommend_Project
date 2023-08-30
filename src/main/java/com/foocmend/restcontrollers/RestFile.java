package com.foocmend.restcontrollers;

import com.foocmend.commons.rests.JSONData;
import com.foocmend.entities.FileInfo;
import com.foocmend.services.file.DownloadFileService;
import com.foocmend.services.file.UploadFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class RestFile {
    private final UploadFileService uploadService;
    private final DownloadFileService downloadService;
    /**
     * 파일 업로드 처리
     * @param files
     * @param gid
     * @param location
     * @return
     */
    @PostMapping("/upload")
    public ResponseEntity<JSONData<List<FileInfo>>> uploadPs(MultipartFile[] files, String gid, String location) {
        List<FileInfo> items = uploadService.upload(files, gid, location);

        JSONData<List<FileInfo>> data = new JSONData<>();
        data.setSuccess(true);
        data.setData(items);

        return ResponseEntity.ok(data);
    }

    @RequestMapping("/download/{id}")
    public void download(@PathVariable Long id) {
        downloadService.download(id);
    }
}
