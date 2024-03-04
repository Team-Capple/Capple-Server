package com.server.capple.domain.answer.service;

import com.server.capple.domain.answer.dto.AnswerRequest;
import com.server.capple.domain.answer.dto.AnswerResponse;
import com.server.capple.domain.answer.dto.AnswerResponse.AnswerList;
import com.server.capple.domain.answer.entity.Answer;
import com.server.capple.domain.member.entity.Member;
import org.springframework.data.domain.Pageable;


public interface AnswerService {
    AnswerResponse.AnswerId createAnswer(Member member, Long questionId, AnswerRequest request);
    Answer findAnswer(Long answerId);

    AnswerList getAnswerList(Long questionId, String keyword, Pageable pageable);
}
