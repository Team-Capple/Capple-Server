package com.server.capple.domain.answer.service;

import com.server.capple.domain.answer.dto.AnswerRequest;
import com.server.capple.domain.answer.dto.AnswerResponse;
import com.server.capple.domain.answer.dto.AnswerResponse.MemberAnswerList;
import com.server.capple.domain.answer.dto.AnswerResponse.AnswerList;
import com.server.capple.domain.answer.entity.Answer;
import com.server.capple.domain.member.entity.Member;
import org.springframework.data.domain.Pageable;


public interface AnswerService {
    AnswerResponse.AnswerId createAnswer(Member member, Long questionId, AnswerRequest request);

    Answer findAnswer(Long answerId);

    AnswerResponse.AnswerId updateAnswer(Member member, Long answerId, AnswerRequest request);

    AnswerResponse.AnswerId deleteAnswer(Member loginMember, Long answerId);

    AnswerResponse.AnswerLike toggleAnswerHeart(Member loginMember, Long answerId);

    AnswerList getAnswerList(Long questionId, String keyword, Pageable pageable);

    MemberAnswerList getMemberAnswer(Member member);

    MemberAnswerList getMemberHeartAnswer(Member member);
}
