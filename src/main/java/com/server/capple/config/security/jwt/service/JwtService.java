package com.server.capple.config.security.jwt.service;

import com.server.capple.domain.member.dto.MemberResponse;
import com.server.capple.domain.member.entity.Role;
import org.springframework.security.core.Authentication;

public interface JwtService {
    String createJwt(Long memberId, String role, String tokenType);
    String createJwtFromEmail(String email);
    String createSignUpAccessJwt(String sub);
    Authentication getAuthentication(String token);
    String getSub(String token);
    String getTokenType(String token);
    Integer getMemberId(String token);
    String getRole(String token);
    Boolean isExpired(String token);
    Boolean checkJwt(String token);
    MemberResponse.Tokens refreshTokens(Long memberId, Role role);
    String createApnsJwt();
}
