package com.server.capple.domain.answer.mapper;

import com.server.capple.domain.answer.entity.Answer;
import com.server.capple.domain.answer.entity.AnswerHeart;
import com.server.capple.domain.board.entity.Board;
import com.server.capple.domain.board.entity.BoardHeart;
import com.server.capple.domain.member.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class AnswerHeartMapper {
    public AnswerHeart toAnswerHeart(Answer answer, Member member) {
        return AnswerHeart.builder()
                .answer(answer)
                .member(member)
                .isLiked(false)
                .build();
    }
}