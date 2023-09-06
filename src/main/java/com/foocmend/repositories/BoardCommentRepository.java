package com.foocmend.repositories;

import com.foocmend.entities.BoardComment;
import com.foocmend.entities.QBoardComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface BoardCommentRepository extends JpaRepository<BoardComment, Long>, QuerydslPredicateExecutor<BoardComment> {

    default int getTotalComments(Long boardDataId) {
        QBoardComment boardComment = QBoardComment.boardComment;

        return (int)count(boardComment.boardData.id.eq(boardDataId));
    }
}
