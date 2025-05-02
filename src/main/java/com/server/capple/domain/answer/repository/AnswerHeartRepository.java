package com.server.capple.domain.answer.repository;

import com.server.capple.domain.answer.entity.Answer;
import com.server.capple.domain.answer.entity.AnswerHeart;
import com.server.capple.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnswerHeartRepository extends JpaRepository<AnswerHeart, Long> {
    Optional<AnswerHeart> findByMemberAndAnswer(Member member, Answer answer);
}
