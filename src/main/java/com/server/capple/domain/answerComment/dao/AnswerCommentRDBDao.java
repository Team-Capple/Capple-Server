package com.server.capple.domain.answerComment.dao;

import com.server.capple.domain.answerComment.entity.AnswerComment;
import com.server.capple.domain.member.entity.Member;

import java.time.LocalDateTime;

public class AnswerCommentRDBDao {
    public interface AnswerCommentInfoInterface {
        public AnswerComment getAnswerComment();
        public Member getWriter();
        public String getContent();
        public LocalDateTime getCreatedAt();
        public Boolean getIsLiked();
    }
}
