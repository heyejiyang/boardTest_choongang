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
    private final PasswordEncoder passwordEncoder; //비밀번호 해시화 인터페이스

    /**
     * 회원가입 처리
     * @param form
     */
    public void save(RequestJoin form){
        Member member = new ModelMapper().map(form, Member.class);
        /*
        * ModelMapper: 객체 간의 매핑을 자동으로 처리해주는 라이브러리
        * DTO와 엔티티 간의 변환을 쉽게 처리할 수 있게 도와준다.
        * - 자동매핑: 필드 이름이 일치하는 객체 간에 자동으로 값 복사
        * - 양방향 매핑
        * */
        String hash = passwordEncoder.encode(form.getPassword()); //BCcrypt 해시화
        member.setPassword(hash); //db password 값 해시화 된 비밀번호로 대입

        save(member, List.of(Authority.USER)); //사용자 권한
    }

    //회원정보 저장, 해당 회원 권한 업데이트
    public void save(Member member, List<Authority> authorities){

        //휴대전화번호 숫자만 기록
        String mobile = member.getMobile();
        if(StringUtils.hasText(mobile)){ //비어있지 않은지 검사
            mobile = mobile.replaceAll("\\D",""); //문자 제거
            member.setMobile(mobile); //문자 제거된, 즉 숫자만 있는 전화번호 형태로 mobile 필드 대입
        }

        memberRepository.saveAndFlush(member); //db에 member 객체 저장 후 커밋

        //권한 추가, 수정
        if(authorities != null){
            List<Authorities> items = authoritiesRepository.findByMember(member); //회원의 기존 권한 목록 조회
            authoritiesRepository.deleteAll(items); //삭제
            authoritiesRepository.flush();

            //새 권한 생성 후 저장
            items = authorities.stream().map(a -> Authorities.builder()
                    .member(member)
                    .authority(a)
                    .build()).toList();

            authoritiesRepository.saveAllAndFlush(items);
        }
    }

}

