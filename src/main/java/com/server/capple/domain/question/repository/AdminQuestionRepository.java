package com.server.capple.domain.question.repository;

import com.server.capple.domain.question.entity.Question;
import com.server.capple.domain.question.entity.QuestionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminQuestionRepository extends JpaRepository<Question, Long> {
    Optional<Question> findByQuestionStatus(QuestionStatus questionStatus);
}
