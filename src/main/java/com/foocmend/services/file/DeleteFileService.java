package com.foocmend.services.file;

import com.foocmend.commons.AuthorizationException;
import com.foocmend.commons.MemberUtil;
import com.foocmend.entities.FileInfo;
import com.foocmend.repositories.FileInfoRepository;
import com.foocmend.services.member.DetailMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class DeleteFileService {
    private final MemberUtil memberUtil;
    private final InfoFileService infoService;
    private final FileInfoRepository repository;

    public void delete(Long id) {
        FileInfo item = infoService.get(id);
        /** 파일 삭제 권한 체크 S */
        Long createdBy = item.getCreatedBy(); // 파일 업로드한 사용자 아이디
        DetailMember member = memberUtil.getMember();
        if (createdBy != null && !memberUtil.isAdmin()
                && (!memberUtil.isLogin()
                || (memberUtil.isLogin() && member.getMemNo().longValue() == createdBy.longValue()))) {

            throw new AuthorizationException("UnAuthorized.delete.file");
        }
        /** 파일 삭제 권한 체크 E */

        /**
         * 1. 파일 삭제
         * 2. thumbs 삭제
         * 3. 파일 정보 삭제
         */

        File file = new File(item.getFilePath());
        if (file.exists()) file.delete();

        String[] thumbsPath = item.getThumbsPath();
        if (thumbsPath != null && thumbsPath.length > 0) {
            Arrays.stream(thumbsPath).forEach(p -> {
                File thumb = new File(p);
                if (thumb.exists()) thumb.delete();
            });
        }

        repository.delete(item);
        repository.flush();
    }
}
