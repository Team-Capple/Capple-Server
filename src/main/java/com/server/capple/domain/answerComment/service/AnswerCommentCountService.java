package com.server.capple.domain.answerComment.service;

import com.server.capple.domain.answer.repository.AnswerRepository;
import com.server.capple.domain.answerComment.repository.AnswerCommentRepository;
import com.server.capple.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class AnswerCommentCountService {
    private final AnswerCommentRepository answerCommentRepository;

    @Cacheable(value = "answerComment", key = "#answerId", cacheManager = "oneDayExpireCacheManager")
    public Integer getAnswerCommentCount(Long answerId) {
        return answerCommentRepository.getAnswerCommentCountByAnswerId(answerId);
    }

    @Async
    @CachePut(value = "answerComment", key = "#answerId", cacheManager = "oneDayExpireCacheManager")
    public CompletableFuture<Integer> updateAnswerCommentCount(Long answerId) {
        return CompletableFuture.completedFuture(answerCommentRepository.getAnswerCommentCountByAnswerId(answerId));
    }
}
