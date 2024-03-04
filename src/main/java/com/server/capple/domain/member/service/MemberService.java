package com.server.capple.domain.member.service;

import com.server.capple.domain.member.dto.response.MemberResponse;
import com.server.capple.domain.member.entity.Member;

public interface MemberService {
    MemberResponse.MyPageMemberInfo getMemberInfo(Long memberId);

    Member findMember(Long memberId);
}
