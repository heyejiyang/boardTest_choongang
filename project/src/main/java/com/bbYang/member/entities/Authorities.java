package com.bbYang.member.entities;

import com.bbYang.member.constants.Authority;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Authorities { //Many에 해당
    @Id
    @ManyToOne(fetch= FetchType.LAZY)  //외래키(Member테이블의 기본키 참조)
    private Member member;
    /*
    * 지연 로딩 -> Authorities 엔티티를 로드할때 Member 필드는 즉시 로드되지 않는다.
    * 대신 프록시 객체로 대체됨, 프록시 객체는 실제 Member 엔티티의 데이터가 아니라 데이터베이스에서 해당 데이터를 가져오는 방법을 알고 있는 가짜 객체임
    *
    * Member 필드에 접근하는 코드가 실행될 때, 즉 authorities.getMember().getName()과 같이 Member 엔티티의 데이터를 요청할 때 Member 엔티티의 데이터를 가져옴 -> 필요한 시점에 로드
    * */

    @Id
    @Column(length=20)
    @Enumerated(EnumType.STRING) //Enum 타입을 데이터베이스에 저장할때 String 형태로 저장
    private Authority authority;
}
