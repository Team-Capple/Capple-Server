package com.server.capple.config.apns.config;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.netty.http.HttpProtocol;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import javax.net.ssl.SSLException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class ApnsClientConfig {
    @Bean("apnsSslContext")
    public SslContext getApnsSslContext() throws SSLException {
        return SslContextBuilder.forClient().protocols("TLSv1.2").build(); // SSL 설정
    }

    @Bean("apnsConnectionProvider")
    public ConnectionProvider getApnsConnectionProvider() {
        return ConnectionProvider.builder("apns")
            .maxConnections(10) // 최대 커낵션 수
            .pendingAcquireMaxCount(-1) // 재시도 횟수 (-1 : 무한대)
            .pendingAcquireTimeout(java.time.Duration.ofSeconds(10)) // 커넥션 풀에 사용 가능한 커넥션 없을 때의 대기 시간
            .maxIdleTime(java.time.Duration.ofSeconds(5)) // 최대 유휴 시간
            .maxLifeTime(java.time.Duration.ofSeconds(300)) // 최대 생명 시간
            .lifo() // 후입선출
            .build();
    }

    @Bean("apnsH2HttpClient")
    public HttpClient getApnsH2HttpClient(ConnectionProvider apnsConnectionProvider, SslContext apnsSslContext) {
        return HttpClient.create(apnsConnectionProvider) // reactor HttpClient 생성
            .keepAlive(true) // keep-alive 활성화
            .protocol(HttpProtocol.H2) // HTTP/2 활성화
            .doOnConnected(connection -> connection.addHandlerLast(new WriteTimeoutHandler(10, TimeUnit.SECONDS))) // 쓰기 타임 아웃
            .responseTimeout(Duration.ofSeconds(10)) // 응답 타임 아웃
            .secure(sslSpec -> sslSpec.sslContext(apnsSslContext)); // SSL 활성화
    }
}
