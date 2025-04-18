package com.server.capple.domain.questionSubcribeMember.service;

import com.server.capple.domain.answer.entity.Answer;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.notifiaction.service.NotificationService;
import com.server.capple.domain.question.entity.Question;
import com.server.capple.domain.questionSubcribeMember.entity.QuestionSubscribeMember;
import com.server.capple.domain.questionSubcribeMember.repository.QuestionSubscribeMemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class QuestionSubscribeMemberServiceImpl implements QuestionSubscribeMemberService {
    private final QuestionSubscribeMemberRepository questionSubscribeMemberRepository;
    private final NotificationService notificationService;

    @Async
    @Override
    public CompletableFuture<Void> addMemberAndSendLiveAnswerAddedNotification(Member member, Question question, Answer answer) {
        if(sendLiveAnswerAddedNotification(member, question, answer))
            return CompletableFuture.completedFuture(null);
        QuestionSubscribeMember questionSubscribeMember = QuestionSubscribeMember.builder()
            .member(member)
            .question(question)
            .build();
        questionSubscribeMemberRepository.save(questionSubscribeMember);
        return CompletableFuture.completedFuture(null);
    }

    @Override
    @Transactional
    public void removeAll(Question question) {
        questionSubscribeMemberRepository.deleteQuestionSubscribeMemberByQuestion(question);
    }

    @Override
    public boolean sendLiveAnswerAddedNotification(Member actor, Question question, Answer answer) {
        List<Member> subscribers = questionSubscribeMemberRepository.getQuestionSubscribeMemberByQuestion(question).stream().map(QuestionSubscribeMember::getMember).toList();
        List<Member> subscriberExceptActor = subscribers.stream().filter(subscriber -> !subscriber.getId().equals(actor.getId())).toList();
        notificationService.sendLiveAnswerAddedNotification(subscriberExceptActor, question, answer);
        return subscribers.size() != subscriberExceptActor.size();
    }
}
