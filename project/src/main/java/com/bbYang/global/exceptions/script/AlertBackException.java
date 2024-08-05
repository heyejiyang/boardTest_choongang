package com.bbYang.global.exceptions.script;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

// 오류 발생 시에 사용자에게 알림을 주고 뒤로 돌아가기 위한 동작 수행
@Getter
public class AlertBackException extends AlertException{

    private String target; //사용자에게 알림 제공하고 뒤로가기 동작 수행할때 필요

    public AlertBackException(String message, HttpStatus status, String target) {
        super(message,status);

        //target이 없을때는 기본값으로 self 지정
        target = StringUtils.hasText(target) ? target : "self";
        /**
         * self: 기본값, 현재 창에서 연다.
         * parent: 부모 창에서 링크 열기 (기존 창의 앞 창?)
         * blank: 새 창, 새탭에서 링크 열기
         * top: 현재 열려있는 최상위 창에서 링크 열기
         */

        this.target = target;
    }

    public AlertBackException(String message, HttpStatus status){
        this(message,status,null);
    } //매개변수로 message와 status만 받으면 target은 null로 설정된다.
}
