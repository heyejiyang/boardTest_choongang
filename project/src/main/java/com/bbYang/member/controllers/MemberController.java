package com.bbYang.member.controllers;

import com.bbYang.MemberUtil;
import com.bbYang.board.entities.Board;
import com.bbYang.board.repositories.BoardRepository;
import com.bbYang.member.services.MemberSaveService;
import com.bbYang.member.validators.JoinValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
@SessionAttributes("requestLogin") // 지정한 이름과 동일한 이름으로 Model객체에 저장되는 데이터만 HttpSession에 저장, 모델 속성을 세션에 저장
public class MemberController {

    private final JoinValidator joinValidator;
    private final MemberSaveService memberSaveService;
    private final MemberUtil memberUtil;
    private final BoardRepository boardRepository;

    @ModelAttribute //컨트롤러의 모든 요청에서 호출됨, 컨트롤러의 요청 처리 메서드가 호출되기 전에 실행
    private RequestLogin requestLogin(){
        return new RequestLogin(); //RequestLogin 객체를 생성하여 로그인 페이지에서 사용할 수 있도록 한다.
    } //반환하는 객체를 모델에 자동으로 추가, 이 객체는 뷰에서 접근할 수 있는 데이터가 된다.


    @GetMapping("/join")
    public String join(@ModelAttribute RequestJoin form)
    {
        //임시로 에러 던지기
//        boolean result = false;
//        if(!result){
//            throw  new CommonException("테스트 예외", HttpStatus.BAD_REQUEST);
//        }

//        if(!result){
//            throw  new AlertException("Alert 테스트 예외", HttpStatus.BAD_REQUEST);
//        }

//        if(!result){
//            throw  new AlertBackException("AlertBack 테스트 예외", HttpStatus.BAD_REQUEST);
//        }

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
/*
    @ResponseBody
    @GetMapping("/test")
    public void test(Principal principal){
        //로그인한 회원 정보의 아이디 알 수 있다.
        log.info("로그인 아이디:{}",principal.getName());
    }

    @ResponseBody
    @GetMapping("/test2")
    public void test2(@AuthenticationPrincipal MemberInfo memberInfo){
        log.info("로그인 회원: {}", memberInfo.toString());
    }

    @ResponseBody
    @GetMapping("/test3")
    public void test3(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); //로그인 상태에 대한 객체 가져올 수 있다.

        log.info("로그인 상태:{}", authentication.isAuthenticated()); //true,false, 인증권한이 있으면 true, 없으면 false임 로그인 상태 아님

        if(authentication.isAuthenticated() && authentication.getPrincipal() instanceof MemberInfo){ //로그인 상태 - UserDetails 구현체
            MemberInfo memberInfo = (MemberInfo) authentication.getPrincipal();
            log.info("로그인 회원:{}",memberInfo.toString()); //로그인한 회원 정보
        } else { //미로그인 상태 - String 문자열 / anonymousUser (getPrincipal())
            log.info("getPrincipal(): {}",authentication.getPrincipal());
        }

    }

*/

    @ResponseBody
    @GetMapping("/test4")
    public void test4(){
        log.info("로그인 여부: {}",memberUtil.isLogin());
        log.info("로그인 회원: {}",memberUtil.getMember());
        log.info("관리자 권환: {}",memberUtil.isAdmin());
    }

    @ResponseBody
    @GetMapping("/test5")
    public void test5(){
//        Board board = Board.builder()
//                .bId("review")
//                .bName("후기")
//                .build();
//
//        boardRepository.saveAndFlush(board);

        //영속성 상태 안에 있는 엔티티 수정 -> update
        Board board = boardRepository.findById("review").orElse(null);
        board.setBName("수정22_리뷰");
        boardRepository.saveAndFlush(board);
    }

}
