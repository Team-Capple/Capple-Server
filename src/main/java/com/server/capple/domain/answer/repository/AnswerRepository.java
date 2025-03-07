package com.server.capple.domain.answer.repository;

import com.server.capple.domain.answer.dao.AnswerRDBDao.AnswerInfoInterface;
import com.server.capple.domain.answer.dao.AnswerRDBDao.MemberAnswerInfoDBDto;
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

    @Query("""
        SELECT
            a AS answer,
            m.academyGeneration AS writerAcademyGeneration 
        FROM Answer a
        LEFT JOIN Member m ON a.member = m
        WHERE (a.id < :lastIndex OR :lastIndex IS NULL) AND a.id IN :answerIds
        """)
    Slice<MemberAnswerInfoDBDto> findByIdInAndIdIsLessThan(Set<Long> answerIds, Long lastIndex, Pageable pageable);

    boolean existsByQuestionAndMember(Question question, Member member);

    @Query("SELECT a AS answer, " +
        "(r IS NOT NULL) AS isReported, " +
        "a.member.id AS writerId, " +
        "a.member.profileImage AS writerProfileImage, " +
        "a.member.nickname AS writerNickname," +
        "a.member.academyGeneration AS writerAcademyGeneration " +
        "FROM Answer a " +
        "LEFT JOIN " +
        "Report r ON r.answer = a " +
        "WHERE (a.id < :lastIndex OR :lastIndex IS NULL) AND a.question.id = :questionId")
    Slice<AnswerInfoInterface> findByQuestion(@Param("questionId") Long questionId, Long lastIndex, Pageable pageable);

    @Query("""
        SELECT
            a AS answer,
            m.academyGeneration AS writerAcademyGeneration
        FROM Answer a
        LEFT JOIN Member m ON a.member = m
        WHERE (a.id < :lastIndex OR :lastIndex IS NULL) AND a.member = :member
        """)
    Slice<MemberAnswerInfoDBDto> findByMemberAndIdIsLessThan(@Param("member") Member member, Long lastIndex, Pageable pageable);

    @Query("SELECT COUNT(a) FROM Answer a WHERE a.question.id = :questionId")
    Integer getAnswerCountByQuestionId(Long questionId);

    @Query("SELECT COUNT(a.id) FROM Answer a WHERE a.member = :member")
    Integer getAnswerCountByMember(Member member);
}
