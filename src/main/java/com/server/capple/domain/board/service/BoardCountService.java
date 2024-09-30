package com.server.capple.domain.board.service;

import com.server.capple.domain.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardCountService {
    private final BoardRepository boardRepository;

    @Cacheable(value = "boardCount", cacheManager = "noExpireCacheManager")
    public Integer getBoardCount() {
        return boardRepository.getBoardCount();
    }

    @Async
    @CachePut(value = "boardCount", cacheManager = "noExpireCacheManager")
    public CompletableFuture<Integer> updateBoardCount() {
        return CompletableFuture.completedFuture(boardRepository.getBoardCount());
    }
}
