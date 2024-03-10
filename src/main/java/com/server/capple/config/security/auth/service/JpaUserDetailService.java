package com.server.capple.config.security.auth.service;

import com.server.capple.config.security.auth.CustomUserDetails;
import com.server.capple.domain.member.repository.MemberRepository;
import com.server.capple.global.exception.RestApiException;
import com.server.capple.global.exception.errorCode.MemberErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JpaUserDetailService implements UserDetailsService {
    private final MemberRepository memberRepository;
    @Override
    public CustomUserDetails loadUserByUsername(String userId) {
        return memberRepository.findById(Long.valueOf(userId))
            .map(CustomUserDetails::new).orElseThrow(
                () -> new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND)
            );
    }
}
