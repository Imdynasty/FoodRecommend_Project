package com.foocmend.services.board;

import com.foocmend.commons.CommonException;
import org.springframework.http.HttpStatus;

public class GuestPasswordIncorrectException extends CommonException {
    public GuestPasswordIncorrectException() {
        super(bundleValidation.getString("GuestPw.incorrect"), HttpStatus.BAD_REQUEST);
    }
}
