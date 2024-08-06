package com.bbYang.global.advice;

import com.bbYang.MemberUtil;
import com.bbYang.member.entities.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice("com.bbYang") //모든 컨트롤러에서 공통적으로 사용할 수 있는 데이터를 설정하는 역할
@RequiredArgsConstructor
public class CommonControllerAdvice {

    private final MemberUtil memberUtil;

    @ModelAttribute("loggedMember") // 해당 이름으로 뷰에서 사용할 수 있도록 모델에 자동으로 추가
    public Member loggendMember(){
        return memberUtil.getMember(); //현재 로그인한 사용자의 정보를 반환받아 모델에 추가
        //Thymeleaf 템플릿에서는 ${loggedMember}를 사용하여 현재 로그인한 사용자 정보를 접근할 수 있다.
    }

    @ModelAttribute("isLogin")
    public boolean isLogin(){
        return memberUtil.isLogin(); //로그인 상태 확인
    }

    @ModelAttribute("isAdmin")
    public boolean isAdmin(){ //관리자 권한 여부 확인
        return  memberUtil.isAdmin();
    }


}
