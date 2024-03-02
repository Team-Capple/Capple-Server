package com.server.capple.domain.member.service;

import com.server.capple.domain.member.entity.Member;

public interface MemberService {
    Member findMember(Long memberId);
}
