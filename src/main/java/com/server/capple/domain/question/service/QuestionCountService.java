package com.server.capple.domain.question.service;

import com.server.capple.domain.question.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class QuestionCountService {
    private final QuestionRepository questionRepository;

    @Cacheable(value = "boardCount", cacheManager = "noExpireCacheManager")
    public Integer getLiveOrOldQuestionCount() {
        return questionRepository.getLiveOrOldQuestionCount();
    }

    @Async
    @CachePut(value = "boardCount", cacheManager = "noExpireCacheManager")
    public CompletableFuture<Integer> updateLiveOrOldQuestionCount() {
        return CompletableFuture.completedFuture(questionRepository.getLiveOrOldQuestionCount());
    }
}
