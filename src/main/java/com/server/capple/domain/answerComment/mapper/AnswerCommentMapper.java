package com.server.capple.domain.answerComment.mapper;

import com.server.capple.domain.answer.entity.Answer;
import com.server.capple.domain.answerComment.dao.AnswerCommentRDBDao;
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

    public AnswerCommentInfo toAnswerCommentInfo(AnswerCommentRDBDao.AnswerCommentInfoInterface answerCommentInfoDto, Long memberId) {
        return AnswerCommentInfo.builder()
                .answerCommentId(answerCommentInfoDto.getAnswerComment().getId())
                .writerId(answerCommentInfoDto.getWriter().getId())
                .content(answerCommentInfoDto.getAnswerComment().getContent())
                .heartCount(answerCommentInfoDto.getAnswerComment().getHeartCount())
                .isMine(answerCommentInfoDto.getWriter().getId().equals(memberId))
                .isLiked(answerCommentInfoDto.getIsLiked() == null ? false : answerCommentInfoDto.getIsLiked())
                .createdAt(answerCommentInfoDto.getAnswerComment().getCreatedAt())
                .build();
    }
}
