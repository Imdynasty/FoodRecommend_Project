package com.foocmend.commons;

import org.springframework.http.HttpStatus;

public class AlertException extends CommonException {
    public AlertException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
