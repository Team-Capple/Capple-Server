package com.server.capple.support;

import com.server.capple.domain.answer.dto.AnswerRequest;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.member.repository.MemberRepository;
import com.server.capple.domain.question.entity.Question;
import com.server.capple.domain.question.entity.QuestionStatus;
import com.server.capple.domain.question.repository.QuestionRepository;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterEach;
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

    protected Member member;
    protected Question question;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @BeforeEach
    public void setUp() {
        member = createMember();
        question = createQuestion();
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }
    @AfterEach
    public void tearDown() {
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



    protected AnswerRequest getAnswerRequest() {
        return AnswerRequest.builder()
                .answer("나는 와플을 좋아하는 사람이 좋아")
                .tags(List.of("#와플유니버시티", "#와플"))
                .build();
    }

}
