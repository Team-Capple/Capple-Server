package com.server.capple.domain.member.repository;


import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DeviceTokenRedisRepository {
    public static final String DEVICE_TOKEN_KEY = "device-token-";

    @Resource(name = "redisTemplate")
    private ValueOperations<String, String> valueOperations;
    private final RedisTemplate<String, String> redisTemplate;

    public void saveDeviceToken(String key, String deviceToken) {
        valueOperations.set(DEVICE_TOKEN_KEY + key, deviceToken);
    }

    public String getDeviceToken(String key) {
        return valueOperations.get(DEVICE_TOKEN_KEY + key);
    }

    public List<String> getDeviceTokens(List<String> keys) {
        return valueOperations.multiGet(keys.stream().map(key -> DEVICE_TOKEN_KEY + key).toList());
    }

    public void deleteDeviceToken(String key) {
        redisTemplate.delete(DEVICE_TOKEN_KEY + key);
    }
}
