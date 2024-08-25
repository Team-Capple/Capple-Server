package com.server.capple.domain.answerComment.repository;

import com.server.capple.domain.answerComment.entity.AnswerComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AnswerCommentRepository extends JpaRepository<AnswerComment, Long> {
    @Query("SELECT a FROM AnswerComment a WHERE a.answer.id = :answerId ORDER BY a.createdAt")
    List<AnswerComment> findAnswerCommentByAnswerId(Long answerId);
}
