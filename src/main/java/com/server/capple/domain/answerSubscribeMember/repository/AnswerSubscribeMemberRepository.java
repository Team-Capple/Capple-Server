package com.server.capple.domain.answerSubscribeMember.repository;

import com.server.capple.domain.answerSubscribeMember.entity.AnswerSubscribeMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerSubscribeMemberRepository extends JpaRepository<AnswerSubscribeMember, Long> {
    Boolean existsByMemberIdAndAnswerId(Long memberId, Long answerId);
    List<AnswerSubscribeMember> findAnswerSubscribeMembersByAnswerId(Long answerId);
    void deleteAnswerSubscribeMemberByAnswerId(Long answerId);
}
