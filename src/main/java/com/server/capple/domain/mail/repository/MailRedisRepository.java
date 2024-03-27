package com.server.capple.domain.mail.repository;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class MailRedisRepository implements Serializable {
    public static final String EMAIL_CERT_CODE_KEY = "emailCertCode-";
    @Value("${mail.cert-mail-expire-minutes}")
    private Integer certMailExpireMinutes;

    @Resource(name = "redisTemplate")
    private ValueOperations<String, String> valueOperations;

    public Boolean save(String email, String certCode) {
        String key = EMAIL_CERT_CODE_KEY + email;
        Duration timeoutDuration = Duration.ofMinutes(certMailExpireMinutes);
        valueOperations.set(key, certCode, timeoutDuration);
        return true;
    }
}
