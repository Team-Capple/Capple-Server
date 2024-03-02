package com.server.capple.domain.member.repository;

import com.server.capple.domain.member.entity.Member;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("select m from Member m where m.id = :memberId and m.deletedAt is null")
    Optional<Member> findById(@Param("memberId") Long memberId);
}
