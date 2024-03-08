package com.server.capple.domain.member.service;

import com.server.capple.domain.member.dto.MemberRequest;
import com.server.capple.domain.member.dto.MemberResponse;
import com.server.capple.domain.member.entity.Member;
import org.springframework.web.multipart.MultipartFile;


public interface MemberService {
    MemberResponse.MyPageMemberInfo getMemberInfo(Long memberId);
    Member findMember(Long memberId);
    MemberResponse.EditMemberInfo editMemberInfo(Long memberId, MemberRequest.EditMemberInfo request);
    MemberResponse.ProfileImage uploadImage(MultipartFile image);
}
