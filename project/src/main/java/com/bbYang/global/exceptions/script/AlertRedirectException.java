package com.bbYang.global.exceptions.script;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

/**
 * 사용자가 예외 발생 시 특정 URL로 리다이렉션 하도록 하는 예외 클래스
 */
@Getter
public class AlertRedirectException extends AlertException{

    private String url; //리다이렉션 할 url 저장
    private String target; //window.location의 대상 속성 저장

    public AlertRedirectException(String message, String url, HttpStatus status, String target) {
        super(message,status);

        target = StringUtils.hasText(target) ? target : "self"; //target이 없을땐 self
        //target이 비어있지 않거나 공백이 아닌 경우 true 반환


        this.url = url; //리다이렉션 할 url
        this.target = target;
    }
    public AlertRedirectException(String message, String url, HttpStatus status) {
        //타켓이 없을때 고정 - self
        this(message, url, status, null);
    }
}
