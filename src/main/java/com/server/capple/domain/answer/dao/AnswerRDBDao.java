package com.server.capple.domain.answer.dao;

import com.server.capple.domain.answer.entity.Answer;
import com.server.capple.domain.member.entity.AcademyGeneration;

import java.util.Optional;

public class AnswerRDBDao {
    public interface AnswerInfoInterface {
        public Answer getAnswer();
        public Boolean getIsReported();
        public Long getWriterId();
        public String getWriterProfileImage();
        public String getWriterNickname();
        public Optional<AcademyGeneration> getWriterAcademyGeneration();
        public Boolean getIsLiked();
    }

    public interface MemberAnswerInfoDBDto {
        public Answer getAnswer();
        public Optional<AcademyGeneration> getWriterAcademyGeneration();
        public Boolean getIsLiked();
    }
}
