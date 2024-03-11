package com.server.capple.config.security.oauth.apple.vo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "apple-auth")
@Getter
@Setter
public class AppleProperties {
    private String grant_type;
    private String audience;
    private String auth_token_url;
    private String public_key_url;
    private String redirect_uri;
    private String website_url;
    private String client_id;
    private String team_id;
    private String key_id;
    private String private_key;
    private String iss;
}
