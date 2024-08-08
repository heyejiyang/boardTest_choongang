package com.bbYang.global;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// 에러 객체가 넘어오면 메시지 코드를 필드명과 오류 메시지를 같이 담아서 가공한다.
@Component
@RequiredArgsConstructor
public class Utils { // 빈의 이름 - utils

    private final MessageSource messageSource; //메시지 코드로부터 실제 메시지(문자열)을 가져오는데 쓴다.
    private final HttpServletRequest request;

    //접근 방법 알기 위해 임의로 정의 이렇게 해줄필요는 없다 X
    public String toUpper(String str) {
        return str.toUpperCase();
    }

    // Errors 객체를 받아서 필드명과 그에 대한 오류 메시지를 Map으로 반환하는 메서드
    // Errors에는 필드 오류와 글로벌 오류를 포함한다.
    public Map<String, List<String>> getErrorMessages(Errors errors) {
        // FieldErrors
        // FieldError 객체를 가져와서 필드명을 key로, 오류 메시지를 리스트 형태로 value로 가지는 Map으로 변환
        Map<String, List<String>> messages = errors.getFieldErrors()
                .stream()
                .collect(Collectors.toMap(FieldError::getField, e -> getCodeMessages(e.getCodes())));// collect로 모아주고 map 형태로 바꿈
        //getCodes() -> 에러 코드, s 붙어있는 이유는 필드에 해당하는 에러 종류가 여러개일 수 있어서

        // 필드명이 key가 되고 속성값은 에러 메시지, 리스트 형태로 넘어간다.

        // GlobalErrors
        // 특정 필드에 해당하지 않는 글로벌 오류 메시지를 리스트로 변환
        List<String> gMessages = errors.getGlobalErrors()
                .stream()
                .flatMap(e -> getCodeMessages(e.getCodes()).stream()).toList();

        if (!gMessages.isEmpty()) {
            messages.put("global", gMessages);
        }
        return messages;
    }

    // 에러 코드 배열을 받아서 메시지로 변환하는 메서드
    public List<String> getCodeMessages(String[] codes) {
        ResourceBundleMessageSource ms = (ResourceBundleMessageSource) messageSource;
        ms.setUseCodeAsDefaultMessage(false);
        //코드상에서 메시지 보고싶을때?
        //MessageConfig에서 설정해준 기본 메시지가 없을때 에러코드를 메시지코드로 메시지를 대체하는 역할인데 false로 바꿔줌 -> 그럼 없는 코드는 예외발생함 -> 하단 try catch로 처리


        // 메시지를 가져와서 변환
        List<String> messages = Arrays.stream(codes)
                .map(c -> {
                    try {
                        return ms.getMessage(c, null, request.getLocale()); // 메시지 소스에서 코드에 해당하는 메시지를 가져옴
                        // args -> Object 배열로 순서대로 배열 형태로 대체 될 수 있는 부분을 넣음
                        // ex) LOGIN_MSG={0}({1})님 로그인...
                        // 검증 부분에서는 대체될게 없으니 null 넣음
                    } catch (Exception e) {
                        return "";
                    }
                })
                .filter(s -> !s.isBlank()) //빈 문자열이 아닌 메시지 필터링
                .toList();

        ms.setUseCodeAsDefaultMessage(true);
        //싱글톤이기 때문에 다 쓰고 나서 true다시 변환해야함 한쪽이 바뀌면 나머지 템플릿에 쓰는것도 바뀌기 때문에!
        return messages;
    }

    public String getMessage(String code) {
        //코드로 해당하는 메시지를 조회할 수 있는 편의기능

        List<String> messages = getCodeMessages(new String[]{code});

        //메시지가 없으면 code 반환, 있으면 메시지 반환
        return  messages.isEmpty() ? code :messages.get(0);

    }
}
