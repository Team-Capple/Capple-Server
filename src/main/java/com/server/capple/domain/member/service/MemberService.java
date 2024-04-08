package com.server.capple.domain.member.service;

import com.server.capple.domain.member.dto.MemberRequest;
import com.server.capple.domain.member.dto.MemberResponse;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.member.entity.Role;
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
    MemberResponse.Tokens changeRole(Long memberId, Role role);
    MemberResponse.MemberId resignMember (Member member);
    Boolean checkNickname(String nickname);
    Boolean checkEmail(String email);
    Boolean registerEmailWhitelist(String email, Long whitelistDurationMinutes);
    Boolean sendCertMail(String signUpToken, String email);
    Boolean checkCertCode(String signUpToken, String email, String certCode);
}
