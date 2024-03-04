package com.server.capple.domain.member.service;

import com.server.capple.domain.mapper.MemberMapper;
import com.server.capple.domain.member.dto.response.MemberResponse;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.member.repository.MemberRepository;
import com.server.capple.global.exception.RestApiException;
import com.server.capple.global.exception.errorCode.MemberErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper mapper;

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
}