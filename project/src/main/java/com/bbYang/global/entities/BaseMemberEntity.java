package com.bbYang.global.entities;


import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * 회원 정보가 필요한 공통 속성화
 * 모든 엔티티에서 공통적으로 사용하는 속성들을 정의한다.
 */
@MappedSuperclass //다른 엔티티들이 이 클래스를 상속받아 공통 속성을 사용할 수 있도록 함
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseMemberEntity extends BaseEntity{
    @CreatedBy //엔티티가 처음 생성될 때 사용자의 이메일 등을 기록
    @Column(length = 65, updatable = false) //db에 업데이트 될 수 없도록 설정, 즉 엔티티를 업데이트 할 때 이 컬럼의 값이 변경되지 않음
    private String createdBy; //처음 추가할때만 생성

    @LastModifiedBy //마지막으로 수정될 때
    @Column(length = 65, insertable = false) //해당 컬럼이 db에 insert될때 포함되지 않도록 설정 즉, 새 엔티티가 db에 저장될때 컬럼 값이 자동으로 설정되지 않으며 수정될때는 값이 들어간다.
    private String modifiedBy; //수정될때 생성
}
