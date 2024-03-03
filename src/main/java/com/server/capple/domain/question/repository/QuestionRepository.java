package com.server.capple.domain.question.repository;

import com.server.capple.domain.question.entity.Question;
import com.server.capple.domain.question.entity.QuestionStatus;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query("select q from Question q where q.id = :questionId and q.deletedAt is null")
    Optional<Question> findById(@Param("questionId") Long questionId);

//    // 현재 진행중인 질문을 가져오는 메서드
//    @Query("SELECT q FROM Question q WHERE q.questionStatus =: questionStatus AND q.deletedAt IS NULL")
//    Optional<Question> findByLiveQuestion(@Param("questionStatus") String questionStatus);

    // 가장 최근에 지난 질문을 가져오는 메서드
    @Query("SELECT q FROM Question q WHERE q.questionStatus = :questionStatus AND q.deletedAt IS NULL ORDER BY q.livedAt DESC LIMIT 1")
    Optional<Question> findByLastOldQuestion(@Param("questionStatus") QuestionStatus questionStatus);
}
