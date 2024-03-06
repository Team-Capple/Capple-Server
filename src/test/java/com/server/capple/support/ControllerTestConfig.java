package com.server.capple.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.capple.domain.answer.dto.AnswerRequest;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.question.entity.Question;
import com.server.capple.domain.question.entity.QuestionStatus;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class ControllerTestConfig {
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    protected Member member;
    protected Question question;

    @BeforeEach
    public void setUp() {
        member = createMember();
        question = createQuestion();
    }

    protected Member createMember() {
        return Member.builder()
                .email("tnals2384@gmail.com")
                .profileImage("https://owori.s3.ap-northeast-2.amazonaws.com/story/capple_default_image_10635d7a-5f8c-4af2-b062-9a9420634eb3.png")
                .nickname("루시")
                .build();
    }

    protected Question createQuestion() {
        return Question.builder()
                .id(1L)
                .content("아카데미 러너 중 가장 마음에 드는 유형이 있나요?")
                .questionStatus(QuestionStatus.ONGOING)
                .build();
    }

    protected AnswerRequest getAnswerRequest() {
        return AnswerRequest.builder()
                .answer("나는 와플을 좋아하는 사람이 좋아")
                .tags(List.of("#와플유니버시티", "#와플"))
                .build();
    }

}
