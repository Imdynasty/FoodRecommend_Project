package com.foocmend.services.board.config;

import com.foocmend.entities.Board;
import com.foocmend.entities.BoardData;
import com.foocmend.entities.QBoard;
import com.foocmend.entities.QBoardData;
import com.foocmend.repositories.BoardDataRepository;
import com.foocmend.repositories.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeleteBoardConfigService {
    private final BoardRepository repository;
    private final BoardDataRepository dataRepository;

    public void delete(String[] bIds) {
        QBoard board = QBoard.board;
        QBoardData boardData = QBoardData.boardData;
        List<Board> items = (List<Board>)repository.findAll(board.bId.in(bIds));
        repository.deleteAll(items);
        repository.flush();

        List<BoardData> dataItems = (List<BoardData>)dataRepository.findAll(boardData.board.bId.in(bIds));
        dataRepository.deleteAll(dataItems);
        dataRepository.flush();

    }
}
