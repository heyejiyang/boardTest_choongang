package com.bbYang.file.exceptions;

import com.bbYang.global.exceptions.script.AlertBackException;
import org.springframework.http.HttpStatus;

public class FileNotFoundException extends AlertBackException {
    public FileNotFoundException(){
        super("NotFound.file", HttpStatus.NOT_FOUND);
        setErrorCode(true);
    }
}
