package com.server.capple.domain.answerComment.service;

import com.server.capple.domain.answerComment.dto.AnswerCommentRequest;
import com.server.capple.domain.answerComment.dto.AnswerCommentResponse.*;
import com.server.capple.domain.answerComment.entity.AnswerComment;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.global.common.SliceResponse;
import org.springframework.data.domain.Pageable;

public interface AnswerCommentService {

    AnswerComment findAnswerComment(Long answerCommentId);
    AnswerCommentId createAnswerComment(Member member, Long answerId, AnswerCommentRequest request);
    AnswerCommentId deleteAnswerComment(Member member, Long commentId);
    AnswerCommentId updateAnswerComment(Member member, Long commentId, AnswerCommentRequest request);
    AnswerCommentLike toggleAnswerCommentHeart(Member member, Long commentId);
//    SliceResponse<AnswerCommentInfos> getAnswerCommentInfos(Long answerId);
    SliceResponse<AnswerCommentInfo> getAnswerCommentInfos(Long answerId, Long memberId, Long lastIndex, Pageable pageable);

}
