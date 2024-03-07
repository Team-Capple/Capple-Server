package com.server.capple.domain.answer.repository;

import com.server.capple.domain.answer.entity.Answer;
import com.server.capple.domain.member.entity.Member;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    Optional<Answer> findById(Long answerId);

    @Query("SELECT a FROM Answer a WHERE a.question.id = :questionId ORDER BY a.createdAt DESC")
    Optional<List<Answer>> findByQuestion(
            @Param("questionId") Long questionId,
            Pageable pageable);

    @Query("SELECT a FROM Answer a WHERE a.question.id = :questionId AND a.content LIKE %:keyword%")
    Optional<List<Answer>> findByQuestionAndKeyword(
            @Param("questionId") Long questionId,
            @Param("keyword") String keyword,
            Pageable pageable);

    @Query("SELECT a FROM Answer a WHERE a.member = :member and a.deletedAt is null ORDER BY a.createdAt DESC")
    Optional<List<Answer>> findByMember(@Param("member") Member member);
}
