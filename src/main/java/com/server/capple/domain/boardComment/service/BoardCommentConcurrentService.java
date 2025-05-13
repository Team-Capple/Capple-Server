package com.server.capple.domain.boardComment.service;

import com.server.capple.domain.boardComment.entity.BoardComment;
import com.server.capple.domain.boardComment.repository.BoardCommentRepository;
import com.server.capple.global.utils.distributedLock.DistributedLock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardCommentConcurrentService {
    private final BoardCommentRepository boardCommentRepository;

    @DistributedLock("'boardComment::heartCount::' + #boardComment.id")
    Boolean setHeartCount(BoardComment boardComment, boolean isLiked) {
        boardComment = boardCommentRepository.findById(boardComment.getId()).get();
        boardComment.setHeartCount(isLiked);
        return true;
    }
}
