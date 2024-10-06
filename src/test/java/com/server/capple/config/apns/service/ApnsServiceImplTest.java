package com.server.capple.config.apns.service;

import com.server.capple.config.apns.dto.ApnsClientRequest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;


@DisplayName("APNs 서비스로 ")
@SpringBootTest
@ActiveProfiles("test")
class ApnsServiceImplTest {
    @Autowired
    private ApnsService apnsService;

    @Test
    @Disabled
    @DisplayName("메시지를 전송한다.")
    void sendApns() {
        //given
        String simulatorDeviceToken = "{deviceToken}"; // 기기의 deviceToken을 넣어야 작동
        String title = "title";
        String subTitle = "subTitle";
        String body = "body";
        String threadId = "testApnsMessage";
        String targetContentId = "targetContentId";

        //when
        Boolean result = apnsService.sendApns(ApnsClientRequest.SimplePushBody.builder().title(title).subTitle(subTitle).body(body).sound("default").threadId(threadId).targetContentId(targetContentId).build(), List.of(simulatorDeviceToken));

        //then
        assertTrue(result);
    }

    @Test
    @Disabled
    @DisplayName("다수의 메시지를 전송한다.")
    void sendApnsMessages() {
        //given
        String simulatorDeviceToken = "{deviceToken}"; // 기기의 deviceToken을 넣어야 작동
        String title = "title";
        String subTitle = "subTitle";
        String body = "body";
        String threadId = "multipleTestApnsMessages";
        String targetContentId = "targetContentId";
        ArrayList<String> deviceTokens = new ArrayList<>();
        for (int i = 0; i < 100; i++) deviceTokens.add(simulatorDeviceToken);

        //when
        Boolean result = apnsService.sendApns(ApnsClientRequest.SimplePushBody.builder().title(title).subTitle(subTitle).body(body).sound("default").threadId(threadId).targetContentId(targetContentId).build(), deviceTokens);

        //then
        assertTrue(result);
    }
}