package com.bbYang.board.entities;

import com.bbYang.global.entities.BaseMemberEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor @AllArgsConstructor
public class Board extends BaseMemberEntity {

    @Id
    @Column(length = 30)
    private String bId;

    @Column(length = 60, nullable = false)
    private String bName;
}
