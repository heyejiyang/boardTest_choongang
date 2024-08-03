package com.bbYang.member.controllers;

import com.bbYang.member.services.MemberSaveService;
import com.bbYang.member.validators.JoinValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
@SessionAttributes("requestLogin") // 지정한 이름과 동일한 이름으로 Model객체에 저장되는 데이터만 HttpSession에 저장, 모델 속성을 세션에 저장
public class MemberController {

    private final JoinValidator joinValidator;
    private final MemberSaveService memberSaveService;

    @ModelAttribute //컨트롤러의 모든 요청에서 호출됨, 컨트롤러의 요청 처리 메서드가 호출되기 전에 실행
    private RequestLogin requestLogin(){
        return new RequestLogin(); //RequestLogin 객체를 생성하여 로그인 페이지에서 사용할 수 있도록 한다.
    } //반환하는 객체를 모델에 자동으로 추가, 이 객체는 뷰에서 접근할 수 있는 데이터가 된다.


    @GetMapping("/join")
    public String join(@ModelAttribute RequestJoin form)
    {
        return "front/member/join";
    }

    @PostMapping("/join")
    public String joinPs(@Valid RequestJoin form, Errors errors){

        joinValidator.validate(form,errors);
        if(errors.hasErrors()){
            return "front/member/join";
        }

        memberSaveService.save(form);

        return "redirect:/member/login";
    }

    @GetMapping("/login")
    public String login(@Valid @ModelAttribute RequestLogin form, Errors errors){
        /*로그인 실패시 LoginFailureHandler에서 에러 코드 form에 대입됨*/
        String code = form.getCode();
        if(StringUtils.hasText(code)){ //code가 null이 아닐경우
            errors.reject(code,form.getDefaultMessage());

            //비밀번호 만료 에러메시지일 경우
            if(code.equals("CredentialsExpired.Login")){
                return "redirect:/member/password/reset";
            }
        }

        return "front/member/login";
    }

}
