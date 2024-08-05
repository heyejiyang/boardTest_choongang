package com.bbYang.global.exceptions.script;


import com.bbYang.global.exceptions.CommonException;
import org.springframework.http.HttpStatus;

/**
 * 자바 스크립트 형태로 에러 메시지 출력
 */
public class AlertException extends CommonException {
    public AlertException(String message, HttpStatus status){
        super(message, status);
    }
}
