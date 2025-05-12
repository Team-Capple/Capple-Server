package com.server.capple.domain.answerComment.service;

import com.server.capple.domain.answerComment.entity.AnswerComment;
import com.server.capple.domain.answerComment.repository.AnswerCommentRepository;
import com.server.capple.global.utils.distributedLock.DistributedLock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnswerCommentConcurrentService {
    private final AnswerCommentRepository answerCommentRepository;

    @DistributedLock("'answerComment::heartCount::' + #answerComment.id")
    Boolean setHeartCount(AnswerComment answerComment, boolean isLiked) {
        answerComment = answerCommentRepository.findById(answerComment.getId()).get();
        answerComment.setHeartCount(isLiked);
        return true;
    }
}
