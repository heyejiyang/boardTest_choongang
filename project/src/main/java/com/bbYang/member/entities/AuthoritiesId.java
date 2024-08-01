package com.bbYang.member.entities;

import com.bbYang.member.constants.Authority;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class AuthoritiesId { //복합키 정의
    private Member member;
    private Authority authority;
}
