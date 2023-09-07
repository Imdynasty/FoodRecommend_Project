package com.foocmend.services.board.comment;

import com.foocmend.entities.BoardData;
import com.foocmend.repositories.BoardCommentRepository;
import com.foocmend.repositories.BoardDataRepository;
import com.foocmend.services.board.BoardDataNotExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateCommentCountService {
    private final BoardDataRepository repository;
    private final BoardCommentRepository commentRepository;

    public void update(Long id) {
        BoardData data = repository.findById(id).orElseThrow(BoardDataNotExistsException::new);

        int cnt = commentRepository.getTotalComments(id);
        data.setCommentCnt(cnt);

        repository.flush();
    }
}
