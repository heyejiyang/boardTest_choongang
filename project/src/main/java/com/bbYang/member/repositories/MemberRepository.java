package com.bbYang.member.repositories;

import com.bbYang.member.entities.Member;
import com.bbYang.member.entities.QMember;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, QuerydslPredicateExecutor<Member> {

    @EntityGraph(attributePaths = "authorities") //바로 조회 할 엔티티 명시, 즉시 로딩 - Member 엔티티를 조회할 때 authorities를 즉시 로딩
    Optional<Member> findByEmail(String email);

    default boolean exists(String email){
        QMember member = QMember.member;

        return exists(member.email.eq(email));
    }
}

/**
 * JpaRepository
 * save(), findById(), findAll(), delete() ... - 상대적으로 정적인 쿼리에 적합
 * QuerydslPredicateExecutor - 복잡하고 동적인 쿼리에 적합
 * findOne(), findAll(), count(), exists() ...
 */