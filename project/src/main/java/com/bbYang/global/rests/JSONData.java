package com.bbYang.global.rests;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@RequiredArgsConstructor //final, NonNULL 팰드를 매개변수로 받는 생성자를 자동으로 생성해줌
public class JSONData {
    private HttpStatus status = HttpStatus.OK; //200
    private boolean success = true; //요청의 성공 여부
    private Object message; //실패원인 메시지
    @NonNull
    private Object data; //실제 응답 데이터
    //예외 처리시에는 사용되지 않지만 일반적인 성공 응답에서는 실제 데이터가 이 필드에 저장
}
