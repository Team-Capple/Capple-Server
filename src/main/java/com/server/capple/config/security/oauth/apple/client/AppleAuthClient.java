package com.server.capple.config.security.oauth.apple.client;

import com.server.capple.config.feign.AppleFeignClientConfig;
import com.server.capple.config.security.oauth.apple.dto.request.IdTokenRequest;
import com.server.capple.config.security.oauth.apple.dto.response.AppleSocialTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "appleAuthClient", url = "https://appleid.apple.com/auth", configuration = AppleFeignClientConfig.class)
public interface AppleAuthClient {
    @PostMapping(value = "/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    AppleSocialTokenResponse generateAndValidateToken(IdTokenRequest request);
}
