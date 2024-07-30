package org.heyejiTest.board.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.heyejiTest.global.entities.BaseEntity;

@Data
@Entity
@Builder
@NoArgsConstructor @AllArgsConstructor
public class BoardData extends BaseEntity {
    @Id @GeneratedValue
    private Long seq;
    @Column(nullable = false, length = 30)
    private String subject;
    @Column(nullable = false)
    @Lob
    private String content;
    @Column(nullable = false, length = 10)
    private String writer;

}
