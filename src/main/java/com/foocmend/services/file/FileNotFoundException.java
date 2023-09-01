package com.foocmend.services.file;

import com.foocmend.commons.CommonException;
import org.springframework.http.HttpStatus;

public class FileNotFoundException extends CommonException {

    public FileNotFoundException() {
        super(bundleError.getString("NotFound.file"), HttpStatus.BAD_REQUEST);
    }
}