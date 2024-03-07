package com.server.capple.domain.member.repository;

import com.server.capple.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findById(Long memberId);

    @Query("select m from Member m where m.id = :memberId and m.deletedAt is null")
    Optional<Member> findById(@Param("memberId") Long memberId);

    @Query("SELECT COUNT(*) FROM Member m WHERE m.nickname = :nickname and m.id != :memberId and m.deletedAt is null")
    Long countMemberByNickname(@Param("nickname") String nickname, @Param("memberId") Long memberId);


}
