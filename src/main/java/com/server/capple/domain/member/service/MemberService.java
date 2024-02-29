package com.server.capple.domain.member.service;

import com.server.capple.domain.member.dto.response.MemberInfoRes;
import com.server.capple.domain.member.entity.Member;

public interface MemberService {
    Member findByMemberId(Long memberId);
    MemberInfoRes getMemberInfo(Long memberId);
}
