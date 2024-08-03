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
        return false;
    }

    public Member getMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); //현재 인증된 사용자의 Authentication 객체를 가져옴
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof MemberInfo) {
            MemberInfo memberInfo = (MemberInfo) authentication.getPrincipal();

            return memberInfo.getMember();
        }

        return null;
    }
}