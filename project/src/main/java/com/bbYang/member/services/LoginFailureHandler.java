package com.bbYang.member.services;

import com.bbYang.member.controllers.RequestLogin;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

/**
 * 로그인 실패시 유입될 클래스
 */
public class LoginFailureHandler implements AuthenticationFailureHandler {

    //로그인 실패시에 유입되는 메서드
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        //인증 실패시 매개변수 세번째 예외 발생 /AuthenticationException 하위 클래스에 여러 예외들 있음 예를들어 MemberInfo - isEnabled()의 활성화 상태에 따라서 예외가 다르게 발생함 -> DisabledException -> 객체 출처 확인 필요

        HttpSession session = request.getSession();

        RequestLogin form = new RequestLogin();
        //로그인 실패시 어떤 오류가 발생했는지를 표시하는데 사용

        form.setEmail(request.getParameter("email"));
        form.setPassword(request.getParameter("password"));
        //로그인 요청에서 입력된 이메일과 비밀번호를 가져온다.
        //로그인 실패후 입력한 정보를 다시 입력할 필요 하지 않게 하기 위해
        //비밀번호 필드는 지워짐

        if (exception instanceof BadCredentialsException) { // 아이디 또는 비밀번호가 일치하지 않는 경우
            form.setCode("BadCredentials.Login");
        } else if (exception instanceof DisabledException) { // 탈퇴한 회원
            form.setCode("Disabled.Login");
        } else if (exception instanceof CredentialsExpiredException) { // 비밀번호 유효기간 만료
            form.setCode("CredentialsExpired.Login");
        } else if (exception instanceof AccountExpiredException) { // 사용자 계정 유효기간 만료
            form.setCode("AccountExpired.Login");
        } else if (exception instanceof LockedException) { // 사용자 계정이 잠겨있는 경우
            form.setCode("Locked.Login");
        } else { //지정한 에러가 아닐경우 Fail.Login으로 응답코드 설정
            form.setCode("Fail.Login");
        }

        //기본 메시지
        form.setDefaultMessage(exception.getMessage());

        System.out.println(exception);

        form.setSuccess(false);

        session.setAttribute("requestLogin", form); //명칭으로 모델쪽에 통합 되는 것이기때문에 명칭은 동일하게
        //로그인 실패시 RequestLogin 객체를 세션에 저장하여 로그인 페이지로 리다이렉트 할 때 이 정보를 사용할 수 있게 한다.
        // -> 로그인 실패에 대한 정보를 보여주기 위해 사용

        /*실패시 다시 로그인 페이지로 이동*/
        response.sendRedirect(request.getContextPath() + "/member/login");

    }
}
