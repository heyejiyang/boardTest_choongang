package com.bbYang.global.exceptions;

import com.bbYang.global.rests.JSONData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.Map;

public interface RestExceptionProcessor {

    @ExceptionHandler(Exception.class)
    default ResponseEntity<JSONData> errorHandler(Exception e) { //모든 예외를 잡아서 처리함

        Object message = e.getMessage(); //예외 메시지 가져와서 저장

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR; // 500
        if (e instanceof CommonException commonException) {
            status = commonException.getStatus();

            Map<String, List<String>> errorMessages = commonException.getErrorMessages();
            if (errorMessages != null) message = errorMessages;
        }

        JSONData data = new JSONData();
        data.setSuccess(false); //요청 실패
        data.setMessage(message); //예외 메시지 또는 맵 설정
        data.setStatus(status); //필드에 상태코드 설정

        e.printStackTrace();

        return ResponseEntity.status(status).body(data); //반환된 응답은 HTTP 응답으로 변환되어 클라이언트에게 전달
    }
}

