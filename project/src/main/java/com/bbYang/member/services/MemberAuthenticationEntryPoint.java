package com.bbYang.member.services;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * 인가 실패시 유입되는 클래스
 */
public class MemberAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        //예외에 따라서 인증 실패가 어떤 경우인지 알 수 있다.
        /**
         * 회원이 아닌데
         * 회원 전용 페이지로 접근한 경우 -> /mypage -> 로그인 페이지 이동
         * 관리자 페이지로 접근한 경우 -> 응답 코드 401, 에러 페이지 출력
         */

        //요청 url 체크(주소 확인)
        String uri = request.getRequestURI();
        if(uri.contains("/admin")){ //관리자 페이지
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED); //401 응답코드 - 접근 자격 없음
        }else { //회원 전용 페이지

            String qs = request.getQueryString(); //쿼리 문자열 가져옴
            String redirectUrl = uri.replace(request.getContextPath(), ""); //uri에서 context경로 제거
            if(StringUtils.hasText(qs)){
                redirectUrl += "?" + qs; //쿼리문자열이 null이 아닐때 redirectUrl? 뒤에 경로 붙음
            }

            response.sendRedirect(request.getContextPath()+"/member/login?redirectUrl="+redirectUrl);
        }
        /* 미 로그인 상태에서 회원 전용 페이지로 들어가려했을때 회원 권한이 필요해서 로그인 창으로 이동 된다. 그 상태에서 로그인 한 경우 메인 페이지로 이동하는 것 보다 다시 이 전에 접근하려던 페이지로 이동하도록 구현 */
    }
}
