package com.server.capple.domain.answer.service;

import com.server.capple.domain.answer.repository.AnswerRepository;
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
public class AnswerCountService {
    private final AnswerRepository answerRepository;

    @Cacheable(value = "questionAnswer", key = "#questionId", cacheManager = "oneDayExpireCacheManager")
    public Integer getQuestionAnswerCount(Long questionId) {
        return answerRepository.getAnswerCountByQuestionId(questionId);
    }

    @Async
    @CachePut(value = "questionAnswer", key = "#questionId", cacheManager = "oneDayExpireCacheManager")
    public CompletableFuture<Integer> updateQuestionAnswerCount(Long questionId) {
        return CompletableFuture.completedFuture(answerRepository.getAnswerCountByQuestionId(questionId));
    }

    @Cacheable(value = "answerCountByMember", key = "#member.id", cacheManager = "oneDayExpireCacheManager")
    public Integer getAnswerCountByMember(Member member) {
        return answerRepository.getAnswerCountByMember(member);
    }

    @CacheEvict(value = "answerCountByMember", key = "#member.id", cacheManager = "oneDayExpireCacheManager")
    public void expireMembersAnsweredCount(Member member) {}
}
