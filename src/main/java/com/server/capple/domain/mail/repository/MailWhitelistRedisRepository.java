package com.server.capple.domain.mail.repository;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class MailWhitelistRedisRepository implements Serializable {
    public static final String EMAIL_WHITE_LIST_KEY = "mailWhitelist-";

    @Resource(name = "redisTemplate")
    private ValueOperations<String, Boolean> valueOperations;

    public Boolean save(String email, Long whitelistDurationMinutes) {
        String key = EMAIL_WHITE_LIST_KEY + email;
        Duration timeoutDuration = Duration.ofMinutes(whitelistDurationMinutes);
        valueOperations.set(key, true, timeoutDuration);
        return true;
    }

    public Boolean existsByEmail(String email) {
        String key = EMAIL_WHITE_LIST_KEY + email;
        return valueOperations.get(key) != null;
    }
}
