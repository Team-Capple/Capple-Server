package com.server.capple.domain.answer.controller;

import com.server.capple.domain.answer.dto.AnswerRequest;
import com.server.capple.domain.answer.dto.AnswerResponse;
import com.server.capple.domain.answer.service.AnswerService;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.support.ControllerTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("Answer 컨트롤러의")
@SpringBootTest
@AutoConfigureMockMvc
public class AnswerControllerTest extends ControllerTestConfig {

    @MockBean
    private AnswerService answerService;

    @Test
    @DisplayName("답변 생성 API 테스트")
    public void createAnswerTest() throws Exception {
        //given
        final String url = "/answers/{questionId}";

        AnswerRequest request = getAnswerRequest();
        AnswerResponse.AnswerId response = new AnswerResponse.AnswerId(1L);

        doReturn(response).when(answerService).createAnswer(any(Member.class), any(Long.class), any(AnswerRequest.class));

        //when
        ResultActions resultActions = this.mockMvc.perform(post(url, question.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)));

        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))
                .andExpect(jsonPath("$.result.answerId").value(1L));
    }
}
