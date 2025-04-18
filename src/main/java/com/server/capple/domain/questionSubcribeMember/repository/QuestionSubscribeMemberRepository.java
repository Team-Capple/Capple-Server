package com.server.capple.domain.questionSubcribeMember.repository;

import com.server.capple.domain.question.entity.Question;
import com.server.capple.domain.questionSubcribeMember.entity.QuestionSubscribeMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionSubscribeMemberRepository extends JpaRepository<QuestionSubscribeMember, Long> {
    void deleteQuestionSubscribeMemberByQuestion(Question question);
    List<QuestionSubscribeMember> getQuestionSubscribeMemberByQuestion(Question question);
}
