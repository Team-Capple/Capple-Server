package com.server.capple.config.apns.service;

import com.server.capple.config.security.jwt.service.JwtService;
import com.server.capple.domain.member.repository.DeviceTokenRedisRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApnsServiceImpl implements ApnsService {
    private final JwtService jwtService;
    private final HttpClient apnsH2HttpClient;
    private final DeviceTokenRedisRepository deviceTokenRedisRepository;
    private WebClient defaultApnsWebClient;

    @Value("${apns.base-url}")
    private String apnsBaseUrl;
    @Value("${apns.base-sub-url}")
    private String apnsBaseSubUrl;
    @Value("${apple-auth.client_id}")
    private String apnsTopic;
    private final String apnsAlertPushType = "alert";

    @PostConstruct
    public void init() {
        defaultApnsWebClient = WebClient.builder()
            .clientConnector(new ReactorClientHttpConnector(apnsH2HttpClient))
            .baseUrl(apnsBaseUrl)
            .defaultHeader("apns-topic", apnsTopic)
            .defaultHeader("apns-push-type", apnsAlertPushType)
            .build();
    }

    @Override
    public <T> Boolean sendApns(T request, String... deviceToken) {
        return sendApns(request, List.of(deviceToken));
    }

    @Override
    public <T> Boolean sendApns(T request, List<String> deviceToken) {
        WebClient tmpWebClient = defaultApnsWebClient.mutate()
            .defaultHeader("authorization", "bearer " + jwtService.createApnsJwt())
            .build();

        WebClient tmpSubWebClient = tmpWebClient.mutate()
            .baseUrl(apnsBaseSubUrl)
            .build();

        deviceToken.parallelStream()
            .forEach(token -> {
                if (token.isBlank()) return;
                tmpWebClient
                    .method(HttpMethod.POST)
                    .uri(token)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .doOnDiscard(Void.class, response -> {// 거절 시 보조 채널로 재시도
                        tmpSubWebClient
                            .method(HttpMethod.POST)
                            .uri(token)
                            .bodyValue(request)
                            .retrieve()
                            .bodyToMono(Void.class)
                            .subscribe();
                        log.info("APNs 전송 거절 발생");
                    })
                    .doOnError(e -> { // 에러 발생 시 보조 채널로 재시도
                        tmpSubWebClient
                            .method(HttpMethod.POST)
                            .uri(token)
                            .bodyValue(request)
                            .retrieve()
                            .bodyToMono(Void.class)
                            .subscribe();
                        log.error("APNs 전송 중 오류 발생", e);
                    })
                    .subscribe();
            });
        return true;
    }

    @Override
    public <T> Boolean sendApnsToMembers(T request, Long... memberIds) {
        return sendApns(request, deviceTokenRedisRepository.getDeviceTokens(List.of(memberIds)));
    }

    @Override
    public <T> Boolean sendApnsToMembers(T request, List<Long> memberIdList) {
        return sendApns(request, deviceTokenRedisRepository.getDeviceTokens(memberIdList));
    }
}
