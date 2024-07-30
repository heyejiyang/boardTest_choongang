package org.heyejiTest.board.controllers;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RequestBoard { //커맨드 - 검증 용도
    @NotBlank //필수항목 검증
    private String subject;
    @NotBlank
    private String content;
    @NotBlank
    private String writer;
}
