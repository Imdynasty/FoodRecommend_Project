package com.foocmend.services.board.comment;

import com.foocmend.entities.BoardComment;
import com.foocmend.repositories.BoardCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteCommentService {
    private final BoardCommentRepository repository;
    private final UpdateCommentCountService updateCommentCountService;
    public void delete(Long id) {
        BoardComment comment = repository.findById(id).orElseThrow(CommentNotExistsException::new);
        Long boardDataId = comment.getId();

        repository.delete(comment);

        updateCommentCountService.update(boardDataId);
    }
}