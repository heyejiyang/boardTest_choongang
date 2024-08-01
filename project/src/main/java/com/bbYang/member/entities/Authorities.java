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
public class Authorities {
    @Id
    @ManyToOne(fetch= FetchType.LAZY)
    private Member member;

    @Id
    @Column(length=20)
    @Enumerated(EnumType.STRING)
    private Authority authority;
}
