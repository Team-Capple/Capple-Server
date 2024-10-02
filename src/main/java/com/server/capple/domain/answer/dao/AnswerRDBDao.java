package com.server.capple.domain.answer.dao;

import com.server.capple.domain.answer.entity.Answer;

public class AnswerRDBDao {
    public interface AnswerInfoInterface {
        public Answer getAnswer();
        public Boolean getIsReported();
        public Long getWriterId();
        public String getWriterProfileImage();
        public String getWriterNickname();
    }
}
