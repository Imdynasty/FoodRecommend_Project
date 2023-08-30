package com.foocmend.services.board;

import com.foocmend.commons.MemberUtil;
import com.foocmend.entities.BoardData;
import com.foocmend.entities.BoardView;
import com.foocmend.repositories.BoardDataRepository;
import com.foocmend.repositories.BoardViewRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 조회수 업데이트
 *
 */
@Service
@RequiredArgsConstructor
public class UpdateHitService {
    private final BoardViewRepository boardViewRepository;
    private final BoardDataRepository boardDataRepository;
    private final HttpServletRequest request;
    private final MemberUtil memberUtil;

    public void update(Long id) {
        try {
            BoardView boardView = new BoardView();
            boardView.setId(id);
            boardView.setUid(""+getUid());
            boardViewRepository.saveAndFlush(boardView);

        } catch (Exception e) {}

        long cnt = boardViewRepository.getHit(id);
        BoardData boardData = boardDataRepository.findById(id).orElse(null);
        if (boardData != null) {
            boardData.setHit((int)cnt);
            boardDataRepository.flush();
        }

    }

    private int getUid() {
        String ip = request.getRemoteAddr();
        String ua = request.getHeader("User-Agent");

        return memberUtil.isLogin() ? memberUtil.getMember().getMemNo().intValue() : Objects.hash(ip, ua);
    }
}