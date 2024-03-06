package com.server.capple.domain.member.service;

import com.server.capple.domain.member.dto.MemberRequest;
import com.server.capple.domain.member.mapper.MemberMapper;
import com.server.capple.domain.member.dto.MemberResponse;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.member.repository.MemberRepository;
import com.server.capple.global.exception.RestApiException;
import com.server.capple.global.exception.errorCode.MemberErrorCode;
import com.server.capple.global.utils.S3ImageComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    // 기본 프로필 이미지
    // todo: 기본 프로필 디자인 작업 완료 시 변경
    private static final String DEFAULT_PROFILE_IMAGE = "https://capple-bucket.s3.ap-northeast-2.amazonaws.com/capple_default_image.png";

    private final MemberRepository memberRepository;
    private final MemberMapper mapper;
    private final S3ImageComponent s3ImageComponent;

    @Override
    public MemberResponse.MyPageMemberInfo getMemberInfo(Long memberId) {
        Member member = findMember(memberId);
        return mapper.toMyPageMemberInfo(member.getNickname(), member.getEmail(), member.getProfileImage(), changeJoinDateFormat(member.getCreatedAt()));
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
    public MemberResponse.editMemberInfo editMemberInfo(Long memberId, MemberRequest.editMemberInfo request) {
        Member member = findMember(memberId);

        // 이전 이미지 버킷에서 삭제
        s3ImageComponent.deleteImage(member.getProfileImage());

        // 1. 이미지 업데이트
        String imageUrl = Optional.ofNullable(request.getProfileImage()).orElse(DEFAULT_PROFILE_IMAGE);
        member.updateProfileImage(imageUrl);

        // 2. 닉네임 업데이트
        member.updateNickname(request.getNickname());

        return mapper.toEditMemberInfo(member.getId(), member.getNickname(), member.getProfileImage());
    }

    @Override
    @Transactional
    public MemberResponse.ProfileImage uploadImage(MultipartFile image) {
        return new MemberResponse.ProfileImage(s3ImageComponent.uploadImage(image));
    }

}