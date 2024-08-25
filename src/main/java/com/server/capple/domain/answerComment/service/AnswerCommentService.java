package com.server.capple.domain.answerComment.service;

import com.server.capple.domain.answerComment.dto.AnswerCommentRequest;
import com.server.capple.domain.answerComment.dto.AnswerCommentResponse.*;
import com.server.capple.domain.answerComment.entity.AnswerComment;
import com.server.capple.domain.member.entity.Member;

public interface AnswerCommentService {
    AnswerComment findAnswerComment(Long answerCommentId);
    AnswerCommentId createAnswerComment(Member member, Long answerId, AnswerCommentRequest request);
    AnswerCommentId deleteAnswerComment(Member member, Long commentId);
    AnswerCommentId updateAnswerComment(Member member, Long commentId, AnswerCommentRequest request);
    AnswerCommentHeart heartAnswerComment(Member member, Long commentId);
    AnswerCommentInfos getAnswerCommentInfos(Long answerId);
}
