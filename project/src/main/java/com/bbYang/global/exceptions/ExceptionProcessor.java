package com.bbYang.global.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

public interface ExceptionProcessor {

    @ExceptionHandler(Exception.class)
    default ModelAndView errorHandler(Exception e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR; //기본 응답코드는 500
        if(e instanceof CommonException commonException){ //내가 정의한 CommonException의 하위 객체일경우
            status = commonException.getStatus(); //응답코드 가져옴
        }
        String url = request.getRequestURI(); //요청 url
        String qs = request.getQueryString(); //쿼리스트링 있을 경우 붙여서 주소 완성할거임

        if(StringUtils.hasText(qs)) url += "?" + qs; //쿼리스트링 값이 있는 경우 url? 뒤에 추가

        ModelAndView mv = new ModelAndView();
        mv.addObject("message", e.getMessage());
        mv.addObject("status", status.value()); //숫자로 상태코드 담음
        mv.addObject("method",request.getMethod());// Http 요청 메서드 정보
        mv.addObject("path",url);//경로
        mv.setStatus(status); //응답코드 설정
        mv.setViewName("error/error"); //모든 에러는 error.html로 전달

        return mv;
    }
}
