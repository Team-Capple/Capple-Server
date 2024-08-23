package com.server.capple.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.port}")
    private int port;
    @Value("${spring.data.redis.database}")
    private int database;
    @Value("${redis-cloud.host}")
    private String redisCloudHost;
    @Value("${redis-cloud.port}")
    private int redisCloudPort;
    @Value("${redis-cloud.database}")
    private int redisCloudDatabase;
    @Value("${redis-cloud.username}")
    private String redisCloudUsername;
    @Value("${redis-cloud.password}")
    private String redisCloudPassword;

    @Bean
    @Primary
    public RedisConnectionFactory redisConnectionFactory() {
        LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(host, port);
        connectionFactory.setDatabase(database);
        return connectionFactory;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        //일반적인 key:value의 경우 시리얼라이저
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        return redisTemplate;
    }

    @Bean
    public RedisConnectionFactory redisCloudConnectionFactory() {
        RedisStandaloneConfiguration redisConfiguration = new RedisStandaloneConfiguration(redisCloudHost, redisCloudPort);
        redisConfiguration.setUsername(redisCloudUsername);
        redisConfiguration.setPassword(redisCloudPassword);
        LettuceConnectionFactory apnsRedisConnectionFactory = new LettuceConnectionFactory(redisConfiguration);
        apnsRedisConnectionFactory.setDatabase(redisCloudDatabase);
        apnsRedisConnectionFactory.start();
        return apnsRedisConnectionFactory;
    }

    @Bean
    @Qualifier("redisCloudConnectionFactory")
    public CacheManager apnsJwtCacheManager(RedisConnectionFactory redisCloudConnectionFactory) {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
            .entryTtl(Duration.ofMinutes(30));
        return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(redisCloudConnectionFactory).cacheDefaults(redisCacheConfiguration).build();
    }
}
