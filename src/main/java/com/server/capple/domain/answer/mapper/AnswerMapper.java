package com.server.capple.domain.answer.mapper;

import com.server.capple.domain.answer.dto.AnswerRequest;
import com.server.capple.domain.answer.entity.Answer;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.question.entity.Question;
import org.springframework.stereotype.Component;

@Component
public class AnswerMapper {
    public Answer toAnswerEntity(AnswerRequest request, Member member, Question question) {
        return Answer.builder()
                .member(member)
                .question(question)
                .tags(String.join(" ", request.getTags()))
                .content(request.getAnswer())
                .build();
    }

}
