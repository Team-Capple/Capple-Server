package com.server.capple.domain.answerSubscribeMember.service;

import com.server.capple.domain.answer.entity.Answer;
import com.server.capple.domain.member.entity.Member;

import java.util.List;

public interface AnswerSubscribeMemberService {
    void createAnswerSubscribeMember(Member member, Answer answer);
    List<Member> findAnswerSubscribeMembers(Long answerId);
    void deleteAnswerSubscribeMemberByAnswerId(Long answerId);
}
