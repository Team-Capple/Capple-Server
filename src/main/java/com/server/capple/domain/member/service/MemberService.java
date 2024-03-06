package com.server.capple.domain.member.service;

import com.server.capple.domain.member.dto.MemberRequest;
import com.server.capple.domain.member.dto.MemberResponse;
import com.server.capple.domain.member.entity.Member;
import org.springframework.web.multipart.MultipartFile;


public interface MemberService {
    MemberResponse.MyPageMemberInfo getMemberInfo(Long memberId);
    Member findMember(Long memberId);
    // MemberProfileImage findMemberProfileImageByMember(Member member);
    MemberResponse.editMemberInfo editMemberInfo(Long memberId, MemberRequest.editMemberInfo request);
    MemberResponse.ProfileImage uploadImage(MultipartFile image);
}
