package com.server.capple.config.security.oauth.apple.mapper;

import com.server.capple.config.security.oauth.apple.dto.request.IdTokenRequest;
import org.springframework.stereotype.Component;

@Component
public class IdTokensRequestMapper {
    public IdTokenRequest toIdTokenRequestByCode(String clientId, String clientSecret, String code, String grantType, String redirectUri) {
        return IdTokenRequest.builder()
            .client_id(clientId)
            .client_secret(clientSecret)
            .code(code)
            .grant_type(grantType)
            .redirect_uri(redirectUri)
            .build();
    }
    public IdTokenRequest toIdTokenRequestByRefreshToken(String clientId, String clientSecret, String refreshToken, String grantType, String redirectUri) {
        return IdTokenRequest.builder()
            .client_id(clientId)
            .client_secret(clientSecret)
            .refresh_token(refreshToken)
            .grant_type(grantType)
            .redirect_uri(redirectUri)
            .build();
    }
}
