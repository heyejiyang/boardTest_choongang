package com.bbYang;

import com.bbYang.member.constants.Authority;
import com.bbYang.member.entities.Authorities;
import com.bbYang.member.entities.Member;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MemberUtil {

    //로그인 여부 체크
    public boolean isLogin() {
        return getMember() != null;
    }

    //최고관리자 권한 여부 체크
    public boolean isAdmin() {

        if (isLogin()) { //로그인 상태일때
            Member member = getMember();
            List<Authorities> authorities = member.getAuthorities();
            //일정 부분 포함 확인, 한개라도 매칭되면 된다
            return authorities.stream().anyMatch(s -> s.getAuthority() == Authority.ADMIN); //권한 더 있으면 ||(or)로 추가하면 됨
        }
        return false; //관리자 권한 아닐경우
    }

    public Member getMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); //현재 인증된 사용자의 Authentication 객체를 가져옴
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof MemberInfo) { //null이 아니고 인증된 권한이고 UserDetails의 구현체인 클래스의 객체일 경우
            MemberInfo memberInfo = (MemberInfo) authentication.getPrincipal();//해당 객체 MemberInfo형으로 변환

            return memberInfo.getMember(); //memberInfo의 Member 정보 반환
        }

        return null;
    }
}