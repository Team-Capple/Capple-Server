package com.server.capple.domain.question.repository;

import com.server.capple.domain.question.entity.Question;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query("select q from Question q where q.id = :questionId and q.deletedAt is null")
    Optional<Question> findById(@Param("questionId") Long questionId);
}
