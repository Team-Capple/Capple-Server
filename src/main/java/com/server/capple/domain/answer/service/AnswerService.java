package com.server.capple.domain.answer.service;

import com.server.capple.domain.answer.dto.AnswerRequest;
import com.server.capple.domain.answer.dto.AnswerResponse;
import com.server.capple.domain.member.entity.Member;


public interface AnswerService {
    AnswerResponse.AnswerId createAnswer(Member member,Long questionId, AnswerRequest request);
}
