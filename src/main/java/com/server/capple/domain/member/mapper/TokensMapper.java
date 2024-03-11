package com.server.capple.domain.member.mapper;

import com.server.capple.domain.member.dto.MemberResponse;
import org.springframework.stereotype.Component;

@Component
public class TokensMapper {
    public MemberResponse.Tokens toTokens(String accessToken, String refreshToken) {
        return MemberResponse.Tokens.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }
}
