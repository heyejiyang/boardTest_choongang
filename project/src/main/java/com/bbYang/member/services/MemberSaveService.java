package com.bbYang.member.services;

import com.bbYang.member.constants.Authority;
import com.bbYang.member.controllers.RequestJoin;
import com.bbYang.member.entities.Authorities;
import com.bbYang.member.entities.Member;
import com.bbYang.member.repositories.AuthoritiesRepository;
import com.bbYang.member.repositories.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;


@Transactional
@RequiredArgsConstructor
@Service
public class MemberSaveService {
    private final MemberRepository memberRepository;
    private final AuthoritiesRepository authoritiesRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입 처리
     * @param form
     */
    public void save(RequestJoin form){
        Member member = new ModelMapper().map(form, Member.class);

        String hash = passwordEncoder.encode(form.getPassword()); //BCcrypt 해시화
        member.setPassword(hash);

        save(member, List.of(Authority.USER));
    }

    public void save(Member member, List<Authority> authorities){

        //휴대전화번호 숫자만 기록
        String mobile = member.getMobile();
        if(StringUtils.hasText(mobile)){
            mobile = mobile.replaceAll("\\D","");
            member.setMobile(mobile);
        }

        memberRepository.saveAndFlush(member);

        //권한 추가, 수정
        if(authorities != null){
            List<Authorities> items = authoritiesRepository.findByMember(member);
            authoritiesRepository.deleteAll(items);
            authoritiesRepository.flush();

            items = authorities.stream().map(a -> Authorities.builder()
                    .member(member)
                    .authority(a)
                    .build()).toList();

            authoritiesRepository.saveAllAndFlush(items);
        }
    }

}

