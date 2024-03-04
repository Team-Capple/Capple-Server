package com.server.capple.support;

import com.server.capple.domain.answer.dto.AnswerRequest;
import com.server.capple.domain.answer.entity.Answer;
import com.server.capple.domain.answer.repository.AnswerRepository;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.member.repository.MemberRepository;
import com.server.capple.domain.question.entity.Question;
import com.server.capple.domain.question.entity.QuestionStatus;
import com.server.capple.domain.question.repository.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

@SpringBootTest
public abstract class ServiceTestConfig {
    @Autowired
    protected MemberRepository memberRepository;
    @Autowired
    protected QuestionRepository questionRepository;
    @Autowired
    protected AnswerRepository answerRepository;

    protected Member member;
    protected Question question;
    protected Answer answer;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @BeforeEach
    public void setUp() {
        member = createMember();
        question = createQuestion();
        answer = createAnswer();
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    protected Member createMember() {
        return memberRepository.save(
                Member.builder()
                        .email("tnals2384@gmail.com")
                        .nickname("루시")
                        .profileImage("lucy.jpg")
                        .build()
        );
    }

    protected Question createQuestion() {
        return questionRepository.save(
                Question.builder()
                        .content("아카데미 러너 중 가장 마음에 드는 유형이 있나요?")
                        .questionStatus(QuestionStatus.ONGOING)
                        .build()
        );
    }

    protected Answer createAnswer() {
        return answerRepository.save(Answer.builder()
                .content("나는 무자비한 사람이 좋아")
                .tags("#무자비 #와플 ")
                .question(question)
                .member(member)
                .build()
        );
    }

    protected AnswerRequest getAnswerRequest() {
        return AnswerRequest.builder()
                .answer("나는 와플을 좋아하는 사람이 좋아")
                .tags(List.of("#와플유니버시티", "#와플"))
                .build();
    }

}
