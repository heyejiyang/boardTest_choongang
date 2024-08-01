package com.bbYang.member.entities;

import com.bbYang.global.entities.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseEntity {
    @Id
    @GeneratedValue
    private Long seq;
    @Column(length =65,unique = true, nullable = false)
    private String email;
    @Column(length = 65, nullable = false)
    private String password;
    @Column(length = 40, nullable = false)
    private String userName;
    @Column(length = 15, nullable = false)
    private String mobile;

}
