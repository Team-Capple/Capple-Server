package com.server.capple.domain.board.service;

import com.server.capple.domain.board.entity.Board;
import com.server.capple.domain.board.repository.BoardRepository;
import com.server.capple.global.utils.distributedLock.DistributedLock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardConcurrentService {
    private final BoardRepository boardRepository;

    @DistributedLock("'board::heartCount::' + #board.id")
    public Boolean setHeartCount(Board board, boolean isLiked) {
        board = boardRepository.findById(board.getId()).get();
        board.setHeartCount(isLiked);
        return true;
    }

    @DistributedLock("'board::commentCount::' + #board.id")
    public Boolean increaseCommentCount(Board board) {
        board = boardRepository.findById(board.getId()).get();
        board.increaseCommentCount();
        return true;
    }

    @DistributedLock("'board::commentCount::' + #board.id")
    public Boolean decreaseCommentCount(Board board) {
        board = boardRepository.findById(board.getId()).get();
        board.decreaseCommentCount();
        return true;
    }
}
