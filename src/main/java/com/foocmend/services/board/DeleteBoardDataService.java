package com.foocmend.services.board;

import com.foocmend.commons.Utils;
import com.foocmend.controllers.admin.BoardForm;
import com.foocmend.controllers.admin.RestaurantForm;
import com.foocmend.entities.Board;
import com.foocmend.entities.BoardData;
import com.foocmend.entities.Restaurant;
import com.foocmend.repositories.BoardDataRepository;
import com.foocmend.repositories.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeleteBoardDataService {
    private final BoardDataRepository repository;
    private final BoardRepository boardRepository;
    private final Utils utils;

    /**
     * 게시글 삭제
     *
     * @param id 게시글 번호
     * @param isComplete : false - 소프트 삭제, true - 완전 삭제
     */
    public void delete(Long id, boolean isComplete) {
        BoardData boardData = repository.findById(id).orElseThrow(BoardDataNotExistsException::new);

        if (isComplete) { // 완전 삭제
            repository.delete(boardData);
        } else { // 소프트 삭제
            boardData.setDeletedDt(LocalDateTime.now());
        }

        repository.flush();
    }

    public void delete(Long id) {
        delete(id, false);
    }

    public void delete(BoardForm form) {
        List<Integer> chkNos = form.getChkNo();
        List<Board> items = new ArrayList<>();
        for (int chkNo : chkNos) {
            Long bid = Long.valueOf(utils.getParam("bid_" + chkNo));
            Board item = boardRepository.findById(String.valueOf(bid)).orElse(null);
            if (item == null) {

                continue;
            }
            items.add(item);
        }

        boardRepository.deleteAll(items);
        boardRepository.flush();
    }
}
