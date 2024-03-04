package com.server.capple.domain.answer.service;

import com.server.capple.domain.answer.dto.AnswerRequest;
import com.server.capple.domain.answer.dto.AnswerResponse;
import com.server.capple.domain.answer.entity.Answer;
import com.server.capple.domain.member.entity.Member;


public interface AnswerService {
    AnswerResponse.AnswerId createAnswer(Member member,Long questionId, AnswerRequest request);
     Answer findAnswer(Long answerId);
     AnswerResponse.AnswerId updateAnswer(Member member, Long answerId, AnswerRequest request);
     AnswerResponse.AnswerId deleteAnswer(Member loginMember, Long answerId);
    AnswerResponse.AnswerLike toggleAnswerHeart(Member loginMember, Long answerId);


}
