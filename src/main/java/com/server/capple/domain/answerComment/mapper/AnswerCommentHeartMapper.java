package com.server.capple.domain.answerComment.mapper;

import com.server.capple.domain.answer.entity.Answer;
import com.server.capple.domain.answer.entity.AnswerHeart;
import com.server.capple.domain.answerComment.dto.AnswerCommentResponse;
import com.server.capple.domain.answerComment.entity.AnswerComment;
import com.server.capple.domain.answerComment.entity.AnswerCommentHeart;
import com.server.capple.domain.member.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class AnswerCommentHeartMapper {
    public AnswerCommentHeart toAnswerCommentHeart(AnswerComment answerComment, Member member) {
        return AnswerCommentHeart.builder()
                .answerComment(answerComment)
                .member(member)
                .isLiked(false)
                .build();
    }
}
