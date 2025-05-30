package com.server.capple.domain.answerComment.repository;

import com.server.capple.domain.answer.entity.Answer;
import com.server.capple.domain.answer.entity.AnswerHeart;
import com.server.capple.domain.answerComment.entity.AnswerComment;
import com.server.capple.domain.answerComment.entity.AnswerCommentHeart;
import com.server.capple.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnswerCommentHeartRepository extends JpaRepository<AnswerCommentHeart, Long> {

    Optional<AnswerCommentHeart> findByMemberAndAnswerComment(Member member, AnswerComment answerComment);
}
