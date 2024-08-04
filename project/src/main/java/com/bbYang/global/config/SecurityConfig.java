package com.bbYang.global.config;

import com.bbYang.member.services.LoginFailureHandler;
import com.bbYang.member.services.LoginSuccessHandler;
import com.bbYang.member.services.MemberAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * 스프링 시큐리티 전용 설정 클래스
 * - 인증 페이지 무력화 시키기
 */
@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)throws Exception {
        /*로그인 로그아웃 S*/
        http.formLogin(f -> {
            f.loginPage("/member/login") //로그인 데이터를 제출 할 경로(로그인 페이지 url)
                    .usernameParameter("email") //로그인시 이메일 필드 이름
                    .passwordParameter("password") //로그인시 비밀번호 필드 이름, 개발자마다 필드 이름이 다를 수 있어서 알려줘야함
                    .successHandler(new LoginSuccessHandler())//로그인 성공시 유입
                    .failureHandler(new LoginFailureHandler()); //실패시 유입

        });//도메인 별로(영역) 나눠서 설정하도록 람다형태로 설정이 바뀌어있음

        http.logout(f ->{
            f.logoutRequestMatcher(new AntPathRequestMatcher("/member/logout")) //로그아웃 처리 주소
                    .logoutSuccessUrl("/member/login");//로그아웃 성공시 이동할 주소
            //로그 기록하거나 후속 처리가 필요할 경우 핸들러 정의하면 됨
        }); //로그아웃시 세션 무효화 시킴
        /*로그인 로그아웃 E*/

        /* 인가(접근 통제) 설정 S*/
        http.authorizeHttpRequests(a ->{ //~/** -> ~포함 하위경로 전체
            a.requestMatchers("/mypage/**").authenticated() //해당경로는 패턴에 대한 요청이 인증된 사용자만 접근 가능
                    .requestMatchers("/admin/**").hasAnyAuthority("ADMIN")//ADMIN권한이 있는 자만 admin경로 접근 가능
                    .anyRequest().permitAll(); //위의 요청 외의 요청들은 접근 제한 없음
            //해당 주소 어떻게 접근 할 수 있는지 권한에 대한 부분 설정 , authenticated(): 회원 전용, 관리자 페이지, 다른 주소는 모든 접근 권한 허용
        });

        //AuthenticationEntryPoint: 인증이 필요한 리소스에 접근할 때, 인증되지 않은 사용자에게 어떻게 응답할지 결정
        //MemberAuthenticationEntryPoint를 구현하면, Spring Security의 기본 동작을 오버라이드하여 동작을 커스터마이즈 함
        http.exceptionHandling(a ->{
            a.authenticationEntryPoint(new MemberAuthenticationEntryPoint())
                    .accessDeniedHandler((req,res,e)-> {
                        res.sendError(HttpStatus.UNAUTHORIZED.value()); //인증된 상태에서도 권한이 부족하여 접근이 거부되었을 경우
                    });
        });
        /* 인가(접근 통제) 설정 E*/

        return http.build();
    }

    //스프링 시큐리티에 해시화 기능 등록되어있음
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(); //encode 메서드 - 해시화 해줌
    }
}
