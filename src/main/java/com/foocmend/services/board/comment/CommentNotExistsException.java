package com.foocmend.services.board.comment;

import com.foocmend.commons.CommonException;
import org.springframework.http.HttpStatus;

/**
 * 댓글이 없을때 예외
 *
 */
public class CommentNotExistsException extends CommonException {
    public CommentNotExistsException() {
        super(bundleValidation.getString("Validation.comment.notExists"), HttpStatus.NOT_FOUND);
    }
}
