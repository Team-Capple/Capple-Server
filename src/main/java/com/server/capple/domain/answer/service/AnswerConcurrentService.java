package com.server.capple.domain.answer.service;

import com.server.capple.domain.answer.entity.Answer;
import com.server.capple.domain.answer.repository.AnswerRepository;
import com.server.capple.global.utils.distributedLock.DistributedLock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnswerConcurrentService {
    private final AnswerRepository answerRepository;

    @DistributedLock("'answer::heartCount::' + #answer.id")
    public Boolean setHeartCount(Answer answer, boolean isLiked) {
        answer = answerRepository.findById(answer.getId()).get();
        answer.setHeartCount(isLiked);
        return true;
    }

    @DistributedLock("'answer::commentCount::' + #answer.id")
    public Boolean increaseCommentCount(Answer answer) {
        answer = answerRepository.findById(answer.getId()).get();
        answer.increaseCommentCount();
        return true;
    }

    @DistributedLock("'answer::commentCount::' + #answer.id")
    public Boolean decreaseCommentCount(Answer answer) {
        answer = answerRepository.findById(answer.getId()).get();
        answer.decreaseCommentCount();
        return true;
    }
}
