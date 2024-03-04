package com.server.capple.domain.answer.repository;

import com.server.capple.domain.answer.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    Optional<Answer> findById(Long answerId);
}
