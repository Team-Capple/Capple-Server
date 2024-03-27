//package com.server.capple.domain.member.repository;
//
//import jakarta.annotation.Resource;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.redis.core.BoundValueOperations;
//import org.springframework.data.redis.core.ValueOperations;
//import org.springframework.data.repository.CrudRepository;
//import org.springframework.stereotype.Repository;
//
//import java.io.Serializable;
//import java.time.Duration;
//
//@Repository
//@RequiredArgsConstructor
//public class TokenRedisRepository implements Serializable {
//    @Resource(name = "redisTemplate")
//    private ValueOperations<String, String> valueOperations;
//
//    public void saveToken(String key, String memberId, ) {
//        valueOperations.set(key, memberId, new Duration);
//    }
//
//}
