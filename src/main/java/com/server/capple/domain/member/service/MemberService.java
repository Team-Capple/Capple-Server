package com.server.capple.domain.member.service;

import com.server.capple.domain.member.dto.response.MemberResponse;
import com.server.capple.domain.member.entity.Member;

public interface MemberService {
    Member findByMemberId(Long memberId);
    MemberResponse.MyPageMemberInfo getMemberInfo(Long memberId);
}
