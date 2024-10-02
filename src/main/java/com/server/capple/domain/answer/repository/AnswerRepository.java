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

import java.util.Optional;
import java.util.Set;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    Optional<Answer> findById(Long answerId);

    Slice<Answer> findByIdInAndIdIsLessThanEqual(Set<Long> answerIds, Long lastIndex, Pageable pageable);

    boolean existsByQuestionAndMember(Question question, Member member);

    @Query("SELECT a AS answer, " +
        "(r IS NOT NULL) AS isReported, " +
        "a.member.id AS writerId, " +
        "a.member.profileImage AS writerProfileImage, " +
        "a.member.nickname AS writerNickname "+
        "FROM Answer a " +
        "LEFT JOIN " +
        "Report r ON r.answer = a " +
        "WHERE a.id <= :lastIndex AND a.question.id = :questionId")
    Slice<AnswerInfoInterface> findByQuestion(@Param("questionId") Long questionId, Long lastIndex, Pageable pageable);

    Slice<Answer> findByMemberAndIdIsLessThanEqual(@Param("member") Member member, Long lastIndex, Pageable pageable);

    @Query("SELECT COUNT(a) FROM Answer a WHERE a.question.id = :questionId")
    Integer getAnswerCountByQuestionId(Long questionId);
}
