package com.server.capple.domain.member.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("디바이스 토큰 레포지토리의 ")
@SpringBootTest
@ActiveProfiles("test")
class DeviceTokenRedisRepositoryTest {
    @Autowired
    private DeviceTokenRedisRepository deviceTokenRedisRepository;

    private String testKeyPrefix = "testKey-";
    private List<String> testKeys = new ArrayList<>();

    @AfterEach
    void afterEach() {
        testKeys.forEach(deviceTokenRedisRepository::deleteDeviceToken);
    }

    @Test
    @DisplayName("디바이스 토큰 저장, 조회 테스트")
    void saveDeviceTokenTest() {
        //given
        String key = testKeyPrefix + UUID.randomUUID().toString();
        testKeys.add(key);
        String deviceToken = UUID.randomUUID().toString();

        //when
        deviceTokenRedisRepository.saveDeviceToken(key, deviceToken);

        //then
        assertEquals(deviceToken, deviceTokenRedisRepository.getDeviceToken(key));
    }

    @Test
    @DisplayName("디바이스 토큰 삭제 테스트")
    void deleteDeviceTokenTest() {
        //given
        String key = testKeyPrefix + UUID.randomUUID().toString();
        String deviceToken = UUID.randomUUID().toString();

        //when
        deviceTokenRedisRepository.saveDeviceToken(key, deviceToken);
        deviceTokenRedisRepository.deleteDeviceToken(key);

        //then
        assertNull(deviceTokenRedisRepository.getDeviceToken(key));
    }

    @Test
    @DisplayName("디바이스 토큰 여러개 조회 테스트")
    void getDeviceTokensTest() {
        //given
        String key1 = testKeyPrefix + UUID.randomUUID().toString();
        String key2 = testKeyPrefix + UUID.randomUUID().toString();
        String key3 = testKeyPrefix + UUID.randomUUID().toString();
        testKeys.add(key1);
        testKeys.add(key2);
        testKeys.add(key3);
        String deviceToken1 = UUID.randomUUID().toString();
        String deviceToken2 = UUID.randomUUID().toString();
        String deviceToken3 = UUID.randomUUID().toString();

        //when
        deviceTokenRedisRepository.saveDeviceToken(key1, deviceToken1);
        deviceTokenRedisRepository.saveDeviceToken(key2, deviceToken2);
        deviceTokenRedisRepository.saveDeviceToken(key3, deviceToken3);

        //then
        List<String> deviceTokens = deviceTokenRedisRepository.getDeviceTokens(List.of(key1, key2, key3));
        assertEquals(3, deviceTokens.size());
        assertTrue(deviceTokens.contains(deviceToken1));
        assertTrue(deviceTokens.contains(deviceToken2));
        assertTrue(deviceTokens.contains(deviceToken3));
    }

    @Test
    @DisplayName("디바이스 토큰 삭제 후 여러개 조회 테스트")
    void getDeviceTokensAfterDeleteTest() {
        //given
        String key1 = testKeyPrefix + UUID.randomUUID().toString();
        String key2 = testKeyPrefix + UUID.randomUUID().toString();
        String key3 = testKeyPrefix + UUID.randomUUID().toString();
        testKeys.add(key1);
        testKeys.add(key2);
        testKeys.add(key3);
        String deviceToken1 = UUID.randomUUID().toString();
        String deviceToken2 = UUID.randomUUID().toString();
        String deviceToken3 = UUID.randomUUID().toString();

        //when
        deviceTokenRedisRepository.saveDeviceToken(key1, deviceToken1);
        deviceTokenRedisRepository.saveDeviceToken(key2, deviceToken2);
        deviceTokenRedisRepository.saveDeviceToken(key3, deviceToken3);
        deviceTokenRedisRepository.deleteDeviceToken(key2);

        //then
        List<String> deviceTokens = deviceTokenRedisRepository.getDeviceTokens(List.of(key1, key2, key3));
        assertEquals(3, deviceTokens.size());
        assertTrue(deviceTokens.contains(deviceToken1));
        assertFalse(deviceTokens.contains(deviceToken2));
        assertNull(deviceTokens.get(1));
        assertTrue(deviceTokens.contains(deviceToken3));
    }

    @Test
    @DisplayName("디바이스 토큰 중복 저장, 조회 테스트")
    void saveDuplicateDeviceTokenTest() {
        //given
        String key = testKeyPrefix + UUID.randomUUID().toString();
        testKeys.add(key);
        String deviceToken1 = UUID.randomUUID().toString();
        String deviceToken2 = UUID.randomUUID().toString();

        //when
        deviceTokenRedisRepository.saveDeviceToken(key, deviceToken1);
        deviceTokenRedisRepository.saveDeviceToken(key, deviceToken2);

        //then
        assertNotEquals(deviceToken1, deviceTokenRedisRepository.getDeviceToken(key));
        assertEquals(deviceToken2, deviceTokenRedisRepository.getDeviceToken(key));
    }
}