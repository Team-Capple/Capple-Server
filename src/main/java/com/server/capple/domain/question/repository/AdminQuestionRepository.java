package com.server.capple.domain.question.repository;

import com.server.capple.domain.question.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminQuestionRepository extends JpaRepository<Question, Long> {
}
