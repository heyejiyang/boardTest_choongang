package com.bbYang.member.services;

import com.bbYang.MemberInfo;
import com.bbYang.member.constants.Authority;
import com.bbYang.member.entities.Authorities;
import com.bbYang.member.entities.Member;
import com.bbYang.member.repositories.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 스프링 시큐리티에서 유저의 정보를 불리오기 위해 구현
 */
@Service
@RequiredArgsConstructor
public class MemberInfoService implements UserDetailsService {

    private final MemberRepository memberRepository;

    //회원 정보가 필요할때마다 호출되는 메서드
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { //유저의 정보를 불러와서 UserDetails로 리턴

        Member member = memberRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username)); //회원 없을 경우 예외 발생

        //MemberInfo쪽에 getAuthorities()메서드를 통해서 사용자 권한 조회, requireNonNullElse -> 첫번째 매개변수가 null일 경우, 두번째 매개변수에서 반환할 기본값 설정
        //권한이 null일때 대체할 기본값을 설정(사용자로)
        //null이 아닌 경우 기존 그대로 반환
        List<Authorities> tmp = Objects.requireNonNullElse(member.getAuthorities(),List.of(Authorities.builder().member(member).authority(Authority.USER)
                .build()));

        //tmp에서 가져온 Authorities 객체 리스트를 Spring Security가 이해할 수 있는 SimpleGrantedAuthority 객체 리스트로 변환
        //권한의 이름을 문자열로 설정
        List<SimpleGrantedAuthority> authorities = tmp
                .stream().map( a -> new SimpleGrantedAuthority(a.getAuthority()
                        .name()))/*Authority enum의 name 메서드를 호출하여 문자열로 변환(authority는 enum상수로 되어있음)*/.toList(); // 이 정보를 가지고 시큐리티쪽에서 인가함

        return MemberInfo.builder()
                .email(member.getEmail())
                .password(member.getPassword())
                .member(member)
                .authorities(authorities)
                .build();
    }
}
