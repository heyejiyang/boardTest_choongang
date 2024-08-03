package com.bbYang.global.config;

import com.bbYang.MemberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;


/**
 * AuditorAwareImpl 클래스는 AuditorAware<String>을 구현하여 현재 사용자(예: 로그인한 사용자)의 이메일 주소를 제공하는 역할을 한다.
 */
//AuditorAware: Entity의 생성 및 수정 정보를 자동으로 추적하고 기록하는 기능 제공
@Component
@RequiredArgsConstructor
public class AuditorAwareImpl implements AuditorAware<String> { //데이터베이스에 엔티티의 생성자와 수정자를 자동으로 기록할 수 있다.

    private final MemberUtil memberUtil;

    @Override
    public Optional<String> getCurrentAuditor() {
        //로그인시 회원정보에서 이메일 가져오고 로그인 아닐시 null값

        // memberUtil.isLogin() -> getMember가 존재할 경우, 즉 로그인 한 상태의 회원 정보가 존재할경우
        //memberUtil.getMember() -> memberInfo의 Member 객체 정보 -> 에서 email 조회
        String email = memberUtil.isLogin() ? memberUtil.getMember().getEmail() : null;
        // 미로그인 상태일때는 null 대입

        return Optional.ofNullable(email);
    }
}
