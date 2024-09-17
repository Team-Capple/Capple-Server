package com.server.capple.domain.answer.service;

import com.server.capple.domain.answer.dto.AnswerRequest;
import com.server.capple.domain.answer.dto.AnswerResponse;
import com.server.capple.domain.answer.dto.AnswerResponse.AnswerInfo;
import com.server.capple.domain.answer.dto.AnswerResponse.MemberAnswerInfo;
import com.server.capple.domain.answer.entity.Answer;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.global.common.SliceResponse;
import org.springframework.data.domain.Pageable;


public interface AnswerService {
    AnswerResponse.AnswerId createAnswer(Member member, Long questionId, AnswerRequest request);

    Answer findAnswer(Long answerId);

    AnswerResponse.AnswerId updateAnswer(Member member, Long answerId, AnswerRequest request);

    AnswerResponse.AnswerId deleteAnswer(Member loginMember, Long answerId);

    AnswerResponse.AnswerLike toggleAnswerHeart(Member loginMember, Long answerId);

    SliceResponse<AnswerInfo> getAnswerList(Long memberId, Long questionId, Pageable pageable);

    SliceResponse<MemberAnswerInfo> getMemberAnswer(Member member, Pageable pageable);

    SliceResponse<MemberAnswerInfo> getMemberHeartAnswer(Member member, Pageable pageable);
}
