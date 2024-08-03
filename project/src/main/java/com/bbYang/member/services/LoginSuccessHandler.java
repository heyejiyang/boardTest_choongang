package com.bbYang.member.services;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * 성공시 유입되는 클래스
 * Spring Security에서 사용자가 로그인 성공했을 때 처리할 동작을 정의하는 핸들러
 */
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    //로그인 성공시 호출되는 메서드
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        //Authentication: 로그인한 사용자의 인증 정보가 담겨있다.

        HttpSession session = request.getSession(); // 사용자의 세션 정보 가져옴
        session.removeAttribute("requestLogin"); //로그인 성공 후 로그인 요청 처리 과정에서 저장한 데이터 지우기

        //로그인 성공시 - redirectUrl이 있으면 해당 주소로 이동, 아니면 메인 페이지로 이동
        String redirectUrl = request.getParameter("redirectUrl"); //클라이언트 요청에서 쿼리 파라미터 가져옴 -> redirectUrl이라는 이름에 저장시켜서 가져왔다.
        redirectUrl = StringUtils.hasText(redirectUrl) ? redirectUrl.trim() : "/";
        //redirectUrl이 null이 아니고 공백문자도 아닐 경우 좌우 공백만 제거해서 저장, 아닐경우 기본메인 페이지로 경로 저장

        response.sendRedirect(request.getContextPath()+redirectUrl);
        //컨텍스트 경로 붙여서 지정된 페이지로 이동하도록 리다이렉트 실행

    }

}
