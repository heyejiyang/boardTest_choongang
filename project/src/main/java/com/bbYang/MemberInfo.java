package com.bbYang;

import com.bbYang.member.entities.Member;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * VO - 수정 불가, 읽기만 가능
 */
@Data
@Builder
public class MemberInfo implements UserDetails {
    /**
     * UserDetails: 스프링 시큐리티에서 사용자의 정보를 담는 인터페이스
     * 스프링 시큐리티가 사용자 인증 정보를 관리하고 인증 절차를 수행하는데 필요한 사용자의 정보를 제공해준다.
     */
    private String email;
    private String password;
    private Member member;
    private Collection<? extends GrantedAuthority> authorities;

    //사용자의 권한을 스프링 시큐리티가 인식 할 수 있도록 해줌
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return "";
    }

    //계정이 만료되지 않았는지 여부를 반환
    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    //계정이 잠겨있지 않은지 여부를 반환
    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    //비밀번호가 만료되지 않았는지 여부를 반환
    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    //계정이 활성화 되었는지, 비활성화된 계정은 인증되지 않도록 처리
    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
