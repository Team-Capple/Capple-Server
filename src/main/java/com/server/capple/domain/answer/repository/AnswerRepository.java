package com.server.capple.domain.answer.repository;

import com.server.capple.domain.answer.entity.Answer;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    @Query("select a from Answer a where a.id = :answerId and a.deletedAt is null")
    Optional<Answer> findById(@Param("answerId") Long answerId);
}
