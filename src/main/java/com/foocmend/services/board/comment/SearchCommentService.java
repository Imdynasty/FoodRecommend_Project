package com.foocmend.services.board.comment;

import com.foocmend.entities.BoardComment;
import com.foocmend.repositories.BoardCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchCommentService {
    private final BoardCommentRepository commentRepository;

    public BoardComment get(Long id) {
        BoardComment comment = commentRepository.findById(id).orElseThrow(CommentNotExistsException::new);

        return comment;
    }
}
