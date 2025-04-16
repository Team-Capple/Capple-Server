package com.server.capple.domain.questionSubcribeMember.service;

import com.server.capple.domain.answer.entity.Answer;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.question.entity.Question;

import java.util.concurrent.CompletableFuture;

public interface QuestionSubscribeMemberService {
    CompletableFuture<Void> addMemberAndSendLiveAnswerAddedNotification(Member member, Question question, Answer answer);
    void removeAll(Question question);
    boolean sendLiveAnswerAddedNotification(Member actor, Question question, Answer answer);
}
