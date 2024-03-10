package com.server.capple.config.security.jwt.service;

import com.server.capple.config.security.auth.CustomUserDetails;
import com.server.capple.config.security.auth.service.JpaUserDetailService;
import com.server.capple.config.security.jwt.dto.JwtProperties;
import com.server.capple.domain.member.dto.MemberResponse;
import com.server.capple.domain.member.entity.Role;
import com.server.capple.domain.member.mapper.TokensMapper;
import com.server.capple.global.exception.RestApiException;
import com.server.capple.global.exception.errorCode.AuthErrorCode;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService{
    private SecretKey secretKey;
    private final JwtProperties jwtProperties;
    private final JpaUserDetailService userDetailService;
    private final TokensMapper tokensMapper;

    @PostConstruct
    protected void init() {
        secretKey = new SecretKeySpec(jwtProperties.getJwt_secret().getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS512.key().build().getAlgorithm());
    }

    public String createJwt(Long memberId, String role, String tokenType) {
        Long expiredTime = 0L;
        if (tokenType == "refresh") {
            expiredTime = jwtProperties.getRefresh_expired_time();
        } else if (tokenType == "access") {
            expiredTime = jwtProperties.getAccess_expired_time();
        } else {
            throw new RestApiException(AuthErrorCode.INVALID_TOKEN_TYPE);
        }
        return Jwts.builder()
            .claim("tokenType", tokenType)
            .claim("memberId", memberId)
            .claim("role", role)
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + expiredTime))
            .signWith(SignatureAlgorithm.HS512, secretKey)
            .compact();
    }

    public String createSignUpAccessJwt(String sub) {
        Long expiredTime = jwtProperties.getRefresh_expired_time();
        return Jwts.builder()
            .claim("tokenType", "signUp")
            .claim("sub", sub)
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + expiredTime))
            .signWith(SignatureAlgorithm.HS512, secretKey)
            .compact();
    }

    public Authentication getAuthentication(String token) {
        CustomUserDetails userDetails = userDetailService.loadUserByUsername(Long.toString(getMemberId(token)));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getSub(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("sub", String.class);
    }

    public String getTokenType(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("tokenType", String.class);
    }

    public Integer getMemberId(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("memberId", Integer.class);
    }

    public String getRole(String token) { //TODO 인가 관련 추가 구현 필요
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }

    public Boolean isExpired(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    public Boolean checkJwt(String token) { //TODO 토큰 유효성 검증 로직 수정 필요
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            throw new RestApiException(AuthErrorCode.INVALID_TOKEN);
        } catch (ExpiredJwtException e) {
            throw new RestApiException(AuthErrorCode.EXPIRED_TOKEN);
        } catch (UnsupportedJwtException e) {
            throw new RestApiException(AuthErrorCode.UNSUPPORTED_TOKEN);
        } catch (SignatureException e) {
            throw new RestApiException(AuthErrorCode.WRONG_TOKEN_SIGNITURE);
        } catch (IllegalArgumentException e) {
            throw new RestApiException(AuthErrorCode.EMPTY_TOKEN);
        }
    }

    public MemberResponse.Tokens refreshTokens(Long memberId, Role role) {
        String accessToken = createJwt(memberId, role.getName(), "access");
        String refreshToken = createJwt(memberId, role.getName(), "refresh");
        return tokensMapper.toTokens(accessToken, refreshToken);
    }
}
