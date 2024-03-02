package com.server.capple.domain.member.service;

import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.member.repository.MemberRepository;
import com.server.capple.global.exception.RestApiException;
import com.server.capple.global.exception.errorCode.MemberErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;

    @Override
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(
                () -> new RestApiException(MemberErrorCode.MEMEBER_NOT_FOUND)
        );
    }
}
