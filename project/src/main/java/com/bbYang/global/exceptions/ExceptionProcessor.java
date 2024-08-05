package com.bbYang.global.exceptions;

import com.bbYang.global.exceptions.script.AlertBackException;
import com.bbYang.global.exceptions.script.AlertException;
import com.bbYang.global.exceptions.script.AlertRedirectException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 * 애플리케이션에서 발생하는 예외를 일관된 방식으로 처리하고, 사용자에게 에러 페이지를 반환
 */
public interface ExceptionProcessor {

    @ExceptionHandler(Exception.class) //모든 예외를 처리하는 핸들러 메서드를 정의
    default ModelAndView errorHandler(Exception e, HttpServletRequest request) {

        ModelAndView mv = new ModelAndView();
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR; //기본 응답코드는 500

        String tpl = "error/error"; //기본 템플릿 뷰

        if(e instanceof CommonException commonException){ //내가 정의한 CommonException의 하위 객체일경우 정의된 상태코드 사용
            status = commonException.getStatus(); //응답코드 가져옴

            if(e instanceof AlertException){
                tpl = "common/_execute_script"; //응답 템플릿 - 이 템플릿은 자바스크립트 코드가 포함된 페이지로 사용자에게 경고 메시지나 리다이렉션을 수행할 수 있다.
                String script = String.format("alert('%s');", e.getMessage());

                if(e instanceof AlertBackException alertBackException){
                    script += String.format("%s.history.back();",alertBackException.getTarget()); //이전페이지로 이동
                }

                if(e instanceof AlertRedirectException alertRedirectException){
                    //주어진 URL로 리다이렉션 하는 자바스크립트 생성
                    String url = alertRedirectException.getUrl();
                    if(!url.startsWith("http")){ //외부 URL이 아닌 경우, http로 시작하지 않는 경우
                        url = request.getContextPath() + url;
                    }

                    script += String.format("%s.location.replace('%s');",alertRedirectException.getTarget(), url);
                }

                mv.addObject("script", script);
            }
        }



        String url = request.getRequestURI(); //요청 url
        String qs = request.getQueryString(); //쿼리스트링 있을 경우 붙여서 주소 완성할거임

        if(StringUtils.hasText(qs)) url += "?" + qs; //쿼리스트링 값이 있는 경우 url? 뒤에 추가

        mv.addObject("message", e.getMessage());
        mv.addObject("status", status.value()); //숫자로 상태코드 담음
        mv.addObject("method",request.getMethod());// Http 요청 메서드 정보
        mv.addObject("path",url);//경로
        mv.setStatus(status); //응답코드 설정
        mv.setViewName(tpl); //모든 에러는 error.html로 전달, 일관된 에러 페이지 제공

        return mv;
    }
}
