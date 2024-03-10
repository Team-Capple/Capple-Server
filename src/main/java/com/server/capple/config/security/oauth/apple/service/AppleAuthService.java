package com.server.capple.config.security.oauth.apple.service;

import com.server.capple.config.security.oauth.apple.dto.AppleIdTokenPayload;

public interface AppleAuthService {
    AppleIdTokenPayload get(String authorizationCode);
}
