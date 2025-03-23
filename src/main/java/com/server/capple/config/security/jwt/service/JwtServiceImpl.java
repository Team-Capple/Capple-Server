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
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.Security;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    private SecretKey secretKey;
    private final JwtProperties jwtProperties;
    private final JpaUserDetailService userDetailService;
    private final TokensMapper tokensMapper;

    @Value("${apns.key-id}")
    private String kid;
    @Value("${apple-auth.team_id}")
    private String iss;
    @Value("${apns.key}")
    private String apnsKeyString;

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

    @Override
    public String createJwtFromEmail(String email) {
        SecretKey emailSecretKey = new SecretKeySpec((jwtProperties.getJwt_secret()+email).getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS512.key().build().getAlgorithm());
        return Jwts.builder()
            .setAudience("capple")
            .signWith(SignatureAlgorithm.HS512, emailSecretKey)
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

    public Long getMemberId(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("memberId", Long.class);
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

    @Override
    @Cacheable(value = "ApnsJwt", cacheManager = "apnsJwtCacheManager")
    public String createApnsJwt() {
        return Jwts.builder()
            .header().add("alg", "ES256").add("kid", kid).and()
            .issuer(iss)
            .issuedAt(new Date(System.currentTimeMillis()))
            .signWith(getPrivateKey(), SignatureAlgorithm.ES256)
            .compact();
    }

    private PrivateKey getPrivateKey() {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");
        try {
            byte[] privateKeyBytes = Base64.getDecoder().decode(apnsKeyString);
            PrivateKeyInfo privateKeyInfo = PrivateKeyInfo.getInstance(privateKeyBytes);
            return converter.getPrivateKey(privateKeyInfo);
        } catch (Exception e) {
            throw new RuntimeException("Error converting private key from String", e);
        }
    }
}
