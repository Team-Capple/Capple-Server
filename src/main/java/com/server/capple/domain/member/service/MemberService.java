package com.server.capple.domain.member.service;

import com.server.capple.domain.member.dto.MemberRequest;
import com.server.capple.domain.member.dto.MemberResponse;
import com.server.capple.domain.member.entity.Member;
import org.springframework.web.multipart.MultipartFile;


public interface MemberService {
    MemberResponse.MyPageMemberInfo getMemberInfo(Member member);
    Member findMember(Long memberId);
    MemberResponse.EditMemberInfo editMemberInfo(Member member, MemberRequest.EditMemberInfo request);
    MemberResponse.ProfileImage uploadImage(MultipartFile image);
    MemberResponse.DeleteProfileImages deleteOrphanageImages();
    MemberResponse.SignInResponse signIn(String authorizationCode);
    MemberResponse.Tokens signUp(String signUpToken, String email, String nickname, String profileImage);
    MemberResponse.SignInResponse localSignIn(String testId);

    MemberResponse.MemberId deleteMember (Member member);
}
