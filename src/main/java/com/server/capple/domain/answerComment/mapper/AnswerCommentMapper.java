package com.server.capple.domain.answerComment.mapper;

import com.server.capple.domain.answer.entity.Answer;
import com.server.capple.domain.answerComment.dto.AnswerCommentResponse.*;
import com.server.capple.domain.answerComment.entity.AnswerComment;
import com.server.capple.domain.member.entity.Member;
import org.springframework.stereotype.Component;


@Component
public class AnswerCommentMapper {
    public AnswerComment toAnswerCommentEntity(Member member, Answer answer, String answerComment) {
        return AnswerComment.builder()
                .member(member)
                .answer(answer)
                .content(answerComment)
                .build();
    }

    public AnswerCommentInfo toAnswerCommentInfo(AnswerComment comment) {
        return AnswerCommentInfo.builder()
                .answerCommentId(comment.getId())
                .writerId(comment.getMember().getId())
                .content(comment.getContent())
                .heartCount(comment.getHeartCount())
                .createdAt(comment.getCreatedAt())
                .build();
    }

}
