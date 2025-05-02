package com.server.capple.domain.answerComment.repository;

import com.server.capple.domain.answerComment.dto.AnswerCommentDBResponse.AnswerCommentAuthorNAnswerNQuestionInfo;
import com.server.capple.domain.answerComment.entity.AnswerComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AnswerCommentRepository extends JpaRepository<AnswerComment, Long> {
    @Query("SELECT a FROM AnswerComment a WHERE a.answer.id = :answerId ORDER BY a.createdAt")
    List<AnswerComment> findAnswerCommentByAnswerId(Long answerId);
    @Query("""
    SELECT
        ac answerComment,
        m author,
        a answer,
        q question
        FROM AnswerComment ac
        LEFT JOIN FETCH Member m
            ON ac.member = m
        LEFT JOIN FETCH Answer a
            ON ac.answer = a
        LEFT JOIN FETCH Question q
            ON a.question = q
        WHERE ac = :answerComment
        ORDER BY ac.createdAt DESC
        LIMIT 1
    """)
    Optional<AnswerCommentAuthorNAnswerNQuestionInfo> findAnswerCommentInfo(AnswerComment answerComment);
}
