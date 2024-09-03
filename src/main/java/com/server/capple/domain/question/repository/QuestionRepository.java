package com.server.capple.domain.question.repository;

import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.question.dao.QuestionInfoInterface;
import com.server.capple.domain.question.entity.Question;
import com.server.capple.domain.question.entity.QuestionStatus;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Objects;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query("SELECT q FROM Question q WHERE q.id = :questionId")
    Optional<Question> findById(@Param("questionId") Long questionId);

    // QuestionStatus에 따라 livedAt에 가장 최신인 Question을 가져오는 쿼리
    @Query("SELECT q FROM Question q WHERE q.questionStatus = 'OLD' OR q.questionStatus = 'LIVE' ORDER BY q.livedAt DESC LIMIT 1")
    Optional<Question> findByQuestionStatusIsLiveAndOldOrderByLivedAt();


    @Query("SELECT DISTINCT q AS question, (a IS NOT NULL) AS isAnsweredByMember " +
            "FROM Question q LEFT JOIN Answer a ON q = a.question AND a.deletedAt is NULL AND a.member = :member " +
            "WHERE q.questionStatus = 'OLD' OR q.questionStatus = 'LIVE' " +
            "ORDER BY q.livedAt DESC")
    List<QuestionInfoInterface> findAllByQuestionStatusIsLiveAndOldOrderByLivedAtDesc(@Param("member") Member member);

}

