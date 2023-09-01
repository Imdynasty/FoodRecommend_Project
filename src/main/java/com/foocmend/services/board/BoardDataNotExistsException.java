package com.foocmend.services.board;

import com.foocmend.commons.CommonException;
import org.springframework.http.HttpStatus;

public class BoardDataNotExistsException extends CommonException {
    public BoardDataNotExistsException() {
        super(bundleValidation.getString("Validation.boardData.notExists"), HttpStatus.BAD_REQUEST);
    }
}
