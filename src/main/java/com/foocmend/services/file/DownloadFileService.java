package com.foocmend.services.file;

import com.foocmend.entities.FileInfo;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
@RequiredArgsConstructor
public class DownloadFileService {
    private final HttpServletResponse response;
    private final InfoFileService infoService;

    public void download(Long id) {

        FileInfo item = infoService.get(id);
        String filePath = item.getFilePath();
        File file = new File(filePath);
        if (!file.exists()) { // 파일이 없는 경우
            throw new FileNotFoundException();
        }

        String fileName = item.getFileName();
        try {
            fileName = new String(fileName.getBytes(), "ISO8859_1");
        } catch (UnsupportedEncodingException e) {}

        try(FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis)) {
            OutputStream out = response.getOutputStream();

            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            response.setHeader("Content-Type", "application/octet-stream");
            response.setHeader("Cache-Control", "must-revalidate");
            response.setHeader("Pragma", "public");
            response.setIntHeader("Expires", 0);
            response.setHeader("Content-Length", "" + file.length());

            while(bis.available() > 0) {
                out.write(bis.read());
            }

            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
