package com.bbYang.member.repositories;

import com.bbYang.member.entities.Authorities;
import com.bbYang.member.entities.AuthoritiesId;
import com.bbYang.member.entities.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface AuthoritiesRepository extends JpaRepository<Authorities, AuthoritiesId>, QuerydslPredicateExecutor<Authorities> {//ID는 복합키
    List<Authorities> findByMember(Member member);
}
