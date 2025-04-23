package com.server.capple.domain.answerComment.dto;

import com.server.capple.domain.answer.entity.Answer;
import com.server.capple.domain.answerComment.entity.AnswerComment;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.question.entity.Question;

public class AnswerCommentDBResponse {
    public interface AnswerCommentAuthorNAnswerNQuestionInfo {
        public AnswerComment getAnswerComment();
        public Member getAuthor();
        public Answer getAnswer();
        public Question getQuestion();
    }
}
