package com.server.capple.domain.answer.repository;

import com.server.capple.domain.answer.dao.AnswerRDBDao.AnswerInfoInterface;
import com.server.capple.domain.answer.entity.Answer;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.question.entity.Question;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    Optional<Answer> findById(Long answerId);

    Slice<Answer> findByIdInAndCreatedAtBefore(Set<Long> answerIds, LocalDateTime thresholdDate, Pageable pageable);

    boolean existsByQuestionAndMember(Question question, Member member);

    @Query("SELECT a AS answer, (r IS NOT NULL) AS isReported " +
        "FROM Answer a " +
        "LEFT JOIN " +
        "Report r ON r.answer = a " +
        "WHERE a.createdAt <= :thresholdDate AND a.question.id = :questionId")
    Slice<AnswerInfoInterface> findByQuestion(
        @Param("questionId") Long questionId,
        LocalDateTime thresholdDate,
        Pageable pageable);

    Slice<Answer> findByMemberAndCreatedAtBefore(@Param("member") Member member, LocalDateTime thresholdDate, Pageable pageable);
}
