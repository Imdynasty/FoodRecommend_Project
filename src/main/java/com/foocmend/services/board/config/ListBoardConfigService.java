package com.foocmend.services.board.config;

import com.foocmend.controllers.admin.BoardSearch;
import com.foocmend.entities.Board;
import com.foocmend.entities.QBoard;
import com.foocmend.repositories.BoardRepository;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.data.domain.Sort.Order.asc;
import static org.springframework.data.domain.Sort.Order.desc;

/**
 * 게시판 설정 목록
 *
 */
@Service
@RequiredArgsConstructor
public class ListBoardConfigService {
    private final BoardRepository boardRepository;

    public Page<Board> gets(BoardSearch boardSearch) {
        QBoard board = QBoard.board;

        BooleanBuilder andBuilder = new BooleanBuilder();

        int page = boardSearch.getPage();
        int limit = boardSearch.getLimit();
        page = page < 1 ? 1 : page;
        limit = limit < 1 ? 20 : limit;

        /** 검색 조건 처리 S */
        String sopt = boardSearch.getSopt();
        String skey = boardSearch.getSkey();
        if (sopt != null && !sopt.isBlank() && skey != null && !skey.isBlank()) {
            skey = skey.trim();
            sopt = sopt.trim();

            if (sopt.equals("all")) { // 통합 검색 - bId, bName
                BooleanBuilder orBuilder = new BooleanBuilder();
                orBuilder.or(board.bId.contains(skey))
                        .or(board.bName.contains(skey));
                andBuilder.and(orBuilder);

            } else if (sopt.equals("bId")) { // 게시판 아이디 bId
                andBuilder.and(board.bId.contains(skey));
            } else if (sopt.equals("bName")) { // 게시판명 bName
                andBuilder.and(board.bName.contains(skey));
            }
        }
        /** 검색 조건 처리 E */

        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(desc("createdDt")));
        Page<Board> data = boardRepository.findAll(andBuilder, pageable);

        return data;
    }

    /**
     * 게시판 전체 정보 조회
     * @return
     */
    public List<Board> getAll() {
        return boardRepository.findAll(Sort.by(asc("bName")));
    }
}
