package com.server.capple.domain.answerComment.repository;

import com.server.capple.domain.answerComment.dao.AnswerCommentRDBDao;
import com.server.capple.domain.answerComment.dto.AnswerCommentDBResponse.AnswerCommentAuthorNAnswerNQuestionInfo;
import com.server.capple.domain.answerComment.dto.AnswerCommentResponse;
import com.server.capple.domain.answerComment.entity.AnswerComment;
import com.server.capple.domain.member.entity.Member;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AnswerCommentRepository extends JpaRepository<AnswerComment, Long> {
//    @Query("SELECT ac FROM AnswerComment ac WHERE ac.answer.id = :answerId ORDER BY ac.createdAt")
//    Slice<AnswerCommentRDBDao.AnswerCommentInfoInterface> findAnswerCommentByAnswerId(@Param("answerId") Long answerId, @Param("member") Member member, @Param("lastIndex") Long lastIndex, Pageable pageable);
@Query("SELECT ac AS answerComment, " +
        "ac.member AS writer, " +
        "ac.content AS content, " +
        "ac.heartCount AS heartCount, " +
        "ac.createdAt AS createdAt, " +
        "CASE WHEN h.id IS NOT NULL THEN TRUE ELSE FALSE END AS isLiked " +
        "FROM AnswerComment ac " +
        "LEFT JOIN AnswerCommentHeart h ON h.answerComment = ac AND h.member = :member AND h.isLiked = TRUE " +
        "WHERE ac.answer.id = :answerId AND (:lastIndex IS NULL OR ac.id < :lastIndex) " +
        "ORDER BY ac.createdAt DESC")
Slice<AnswerCommentRDBDao.AnswerCommentInfoInterface> findAnswerCommentByAnswerId(
        @Param("answerId") Long answerId,
        @Param("member") Member member,
        @Param("lastIndex") Long lastIndex,
        Pageable pageable);

    @Query("""
    SELECT
        ac answerComment,
        m author,
        a answer,
        q question
        FROM AnswerComment ac
        LEFT JOIN FETCH Member m
            ON ac.member = m
        LEFT JOIN FETCH Answer a
            ON ac.answer = a
        LEFT JOIN FETCH Question q
            ON a.question = q
        WHERE ac = :answerComment
        ORDER BY ac.createdAt DESC
        LIMIT 1
    """)
    Optional<AnswerCommentAuthorNAnswerNQuestionInfo> findAnswerCommentInfo(AnswerComment answerComment);

    @Query("SELECT COUNT(ac) FROM AnswerComment ac WHERE ac.answer.id = :answerId")
    Integer getAnswerCommentCountByAnswerId(Long answerId);

    @Query("SELECT COUNT(ac.id) FROM AnswerComment ac WHERE ac.member = :member")
    Integer getAnswerCommentCountByMember(Member member);
}
