package com.bbYang.member.controllers;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RequestLogin {
    @NotBlank
    private String email;
    @NotBlank @Size(min = 8,max = 16)
    private String password;

    private boolean success = true; //false일때만 노출시키기

    private String code; //에러코드
    private String defaultMessage; //에러코드 없을때 기본 에러 메시지

    //로그인 성공시 이동할 주소
    private String redirectUrl;
}

