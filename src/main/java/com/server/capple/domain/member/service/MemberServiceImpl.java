package com.server.capple.domain.member.service;

import com.server.capple.config.security.jwt.service.JwtService;
import com.server.capple.config.security.oauth.apple.service.AppleAuthService;
import com.server.capple.domain.member.dto.MemberRequest;
import com.server.capple.domain.member.mapper.MemberMapper;
import com.server.capple.domain.member.dto.MemberResponse;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.member.entity.Role;
import com.server.capple.domain.member.mapper.TokensMapper;
import com.server.capple.domain.member.repository.MemberRepository;
import com.server.capple.global.exception.RestApiException;
import com.server.capple.global.exception.errorCode.MemberErrorCode;
import com.server.capple.global.utils.S3ImageComponent;
import com.server.capple.config.security.oauth.apple.dto.AppleIdTokenPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final S3ImageComponent s3ImageComponent;
    private final TokensMapper tokensMapper;
    private final AppleAuthService appleAuthService;
    private final JwtService jwtService;

    @Override
    public MemberResponse.MyPageMemberInfo getMemberInfo(Member member) {
        return memberMapper.toMyPageMemberInfo(member.getNickname(), member.getProfileImage(), changeJoinDateFormat(member.getCreatedAt()));
    }

    private String changeJoinDateFormat(LocalDateTime createAt){
        return createAt.format(DateTimeFormatter.ofPattern("yyyy.MM.dd")) + " 가입";
    }

    @Override
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

    @Override
    @Transactional
    public MemberResponse.EditMemberInfo editMemberInfo(Member member, MemberRequest.EditMemberInfo request) {
        Member editedMember = findMember(member.getId());

        // 1. 이미지 업데이트
        editedMember.updateProfileImage(request.getProfileImage());

        // 2. 닉네임 업데이트
        if (memberRepository.countMemberByNickname(request.getNickname(), editedMember.getId()) > 0) throw new RestApiException(MemberErrorCode.EXIST_MEMBER_NICKNAME);
        editedMember.updateNickname(request.getNickname());

        return memberMapper.toEditMemberInfo(editedMember.getId(), editedMember.getNickname(), editedMember.getProfileImage());
    }

    @Override
    @Transactional
    public MemberResponse.ProfileImage uploadImage(MultipartFile image) {
        return new MemberResponse.ProfileImage(s3ImageComponent.uploadImage(image));
    }

    @Override
    public MemberResponse.DeleteProfileImages deleteOrphanageImages() {
        // 버킷에 저장된 모든 이미지 조회
        List<String> bucketImages = s3ImageComponent.findAllImageUrls();

        // profile image로 사용되지 않는 이미지 버킷에서 삭제
        List<String> deleteImages = bucketImages.stream()
                .filter(bucketImage -> !memberRepository.existMemberProfileImage(bucketImage))
                .peek(s3ImageComponent::deleteImage)
                .toList();

        return new MemberResponse.DeleteProfileImages(deleteImages);
    }

    public MemberResponse.SignInResponse signIn(String authorizationCode) {
        AppleIdTokenPayload appleIdTokenPayload = appleAuthService.get(authorizationCode);
        Optional<Member> optionalMember = memberRepository.findBySub(appleIdTokenPayload.getSub());
        if (optionalMember.isEmpty()) {
            String signUpToken = jwtService.createSignUpAccessJwt(appleIdTokenPayload.getSub());
            return memberMapper.toSignInResponse(null, signUpToken, false);
        }
        Long memberId = optionalMember.get().getId();
        String role = optionalMember.get().getRole().getName();
        String accessToken = jwtService.createJwt(memberId, role, "access");
        String refreshToken = jwtService.createJwt(memberId, role, "refresh");
        return memberMapper.toSignInResponse(accessToken, refreshToken, true);
    }

    @Override
    @Transactional
    public MemberResponse.Tokens signUp(String signUpToken, String email, String nickname, String profileImage) {
        String sub = jwtService.getSub(signUpToken);
        Member member = memberMapper.createMember(sub, email, nickname, Role.ROLE_ACADEMIER, profileImage);
        memberRepository.save(member);
        Long memberId = member.getId();
        String role = member.getRole().getName();
        String accessToken = jwtService.createJwt(memberId, role, "access");
        String refreshToken = jwtService.createJwt(memberId, role, "refresh");
        return tokensMapper.toTokens(accessToken, refreshToken);
    }

    @Override
    public MemberResponse.SignInResponse localSignIn(String testId) {
        Optional<Member> optionalMember = memberRepository.findBySub(testId);
        if (optionalMember.isEmpty()) {
            String signUpToken = jwtService.createSignUpAccessJwt(testId);
            return memberMapper.toSignInResponse(null, signUpToken, false);
        }
        Long memberId = optionalMember.get().getId();
        String role = optionalMember.get().getRole().getName();
        String accessToken = jwtService.createJwt(memberId, role, "access");
        String refreshToken = jwtService.createJwt(memberId, role, "refresh");
        return memberMapper.toSignInResponse(accessToken, refreshToken, true);
    }

    @Override
    @Transactional
    public MemberResponse.Tokens changeRole(Long memberId, Role role) {
        Member member = findMember(memberId);
        member.updateRole(role);
        String accessToken = jwtService.createJwt(memberId, role.getName(), "access");
        String refreshToken = jwtService.createJwt(memberId, role.getName(), "refresh");
        return tokensMapper.toTokens(accessToken, refreshToken);
    }

    @Override
    @Transactional
    public MemberResponse.MemberId resignMember(Member member) {
        Member resignedMember = memberRepository.findById(member.getId()).orElseThrow(
                () -> new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND));
        resignedMember.resignMember();
        return memberMapper.toMemberId(member);
    }

    @Override
    public Boolean checkNickname(String nickname) {
        return memberRepository.existsMemberByNickname(nickname);
    }
}