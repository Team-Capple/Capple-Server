package com.server.capple.config.security.oauth.apple.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.capple.config.security.oauth.apple.client.AppleAuthClient;
import com.server.capple.config.security.oauth.apple.dto.response.AppleSocialTokenResponse;
import com.server.capple.config.security.oauth.apple.mapper.IdTokensRequestMapper;
import com.server.capple.config.security.oauth.apple.dto.AppleIdTokenPayload;
import com.server.capple.config.security.oauth.apple.vo.AppleProperties;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.security.Security;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AppleAuthServiceImpl implements AppleAuthService {
    private final AppleAuthClient appleAuthClient;
    private final AppleProperties appleProperties;
    private final IdTokensRequestMapper idTokensRequestMapper;
    public AppleIdTokenPayload get(String authorizationCode) {
        AppleSocialTokenResponse response = appleAuthClient.generateAndValidateToken(idTokensRequestMapper.toIdTokenRequestByCode(appleProperties.getClient_id(), generateClientSecret(), authorizationCode, appleProperties.getGrant_type(), appleProperties.getRedirect_uri()));
        String idToken = response.getIdToken();
        return decodePayload(idToken, AppleIdTokenPayload.class);
    }

    private String generateClientSecret() {
        LocalDateTime expiration = LocalDateTime.now().plusMinutes(5);
        return Jwts.builder()
            .setHeaderParam("alg", "ES256")
            .setHeaderParam("kid", appleProperties.getKey_id())
            .setIssuer(appleProperties.getTeam_id())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(java.sql.Timestamp.valueOf(expiration))
            .setAudience(appleProperties.getAudience())
            .setSubject(appleProperties.getClient_id())
            .signWith(getPrivateKey(), SignatureAlgorithm.ES256)
            .compact();
    }

    private PrivateKey getPrivateKey() {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");
        try {
            byte[] privateKeyBytes = Base64.getDecoder().decode(appleProperties.getPrivate_key());
            PrivateKeyInfo privateKeyInfo = PrivateKeyInfo.getInstance(privateKeyBytes);
            return converter.getPrivateKey(privateKeyInfo);
        } catch (Exception e) {
            throw new RuntimeException("Error converting private key from String", e);
        }
    }

    private static <T> T decodePayload(String token, Class<T> targetClass) {
        String[] tokenParts = token.split("\\.");
        String payloadJWT = tokenParts[1];
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String payload = new String(decoder.decode(payloadJWT));
        ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return objectMapper.readValue(payload, targetClass);
        } catch (Exception e) {
            throw new RuntimeException("Error decoding token payload", e);
        }
    }
}
