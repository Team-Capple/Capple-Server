package com.server.capple.domain.board.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BoardType {
    FREEBOARD("자유게시판"),
    HOTBOARD("인기게시판");

    private final String toKorean;
}
