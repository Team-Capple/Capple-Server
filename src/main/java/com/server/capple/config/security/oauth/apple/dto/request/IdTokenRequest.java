package com.server.capple.config.security.oauth.apple.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IdTokenRequest {
    @JsonProperty("client_id")
    private String client_id;
    @JsonProperty("client_secret")
    private String client_secret;
    @JsonProperty("code")
    private String code;
    @JsonProperty("grant_type")
    private String grant_type;
    @JsonProperty("refresh_token")
    private String refresh_token;
    @JsonProperty("redirect_uri")
    private String redirect_uri;
}
