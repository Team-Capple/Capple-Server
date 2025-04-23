package com.server.capple.domain.answerSubscribeMember.service;

import com.server.capple.domain.answer.entity.Answer;
import com.server.capple.domain.answerSubscribeMember.entity.AnswerSubscribeMember;
import com.server.capple.domain.answerSubscribeMember.repository.AnswerSubscribeMemberRepository;
import com.server.capple.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnswerSubscribeMemberServiceImpl implements AnswerSubscribeMemberService {
    private final AnswerSubscribeMemberRepository answerSubscribeMemberRepository;

    @Override
    public void createAnswerSubscribeMember(Member member, Answer answer) {
        if (answerSubscribeMemberRepository.existsByMemberIdAndAnswerId(member.getId(), answer.getId())) {
            return;
        }
        answerSubscribeMemberRepository.save(AnswerSubscribeMember.builder().member(member).answer(answer).build());
    }

    @Override
    public List<Member> findAnswerSubscribeMembers(Long answerId) {
        return answerSubscribeMemberRepository.findAnswerSubscribeMembersByAnswerId(answerId).stream().map(AnswerSubscribeMember::getMember).toList();
    }

    @Override
    public void deleteAnswerSubscribeMemberByAnswerId(Long answerId) {
        answerSubscribeMemberRepository.deleteAnswerSubscribeMemberByAnswerId(answerId);
    }
}
