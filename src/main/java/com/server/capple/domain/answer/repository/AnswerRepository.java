package com.server.capple.domain.answer.repository;

import com.server.capple.domain.answer.entity.Answer;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    @Query("select a from Answer a where a.id = :answerId and a.deletedAt is null")
    Optional<Answer> findById(@Param("answerId") Long answerId);

    @Query("SELECT a FROM Answer a where a.question.id = :questionId AND a.deletedAt IS NULL ORDER BY a.createdAt DESC")
    Optional<List<Answer>> findByQuestion(@Param("questionId") Long questionId, Pageable pageable);
}
