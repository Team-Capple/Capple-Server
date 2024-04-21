package com.server.capple.domain.tag.controller;

import com.server.capple.domain.member.service.MemberService;
import com.server.capple.domain.tag.dto.TagResponse;
import com.server.capple.domain.tag.service.TagService;
import com.server.capple.support.ControllerTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Tag 컨트롤러의")
@SpringBootTest
@AutoConfigureMockMvc
public class TagControllerTest extends ControllerTestConfig {

    @MockBean
    private TagService tagService;
    @MockBean
    private MemberService memberService;

    @Test
    @DisplayName("Tag 검색 테스트")
    @WithMockUser(username = "user")
    public void searchTagsTest() throws Exception {
        //given
        final String url = "/tags/search";
        TagResponse.TagInfos response = new TagResponse.TagInfos(List.of("#와플", "#바나나"));
        doReturn(response).when(tagService).searchTags(any());

        //when
        ResultActions resultActions = this.mockMvc.perform(get(url).param("keyword", "키워드")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + jwt)
        );

        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))
                .andExpect(jsonPath("$.result.tags").exists());
    }

    @Test
    @DisplayName("이 질문 답변에 사람들이 많이 쓴 키워드 조회 테스트")
    @WithMockUser(username = "user")
    public void getPopularTagsTest() throws Exception {
        //given
        final String url = "/tags/{questionId}";
        TagResponse.TagInfos response = new TagResponse.TagInfos(List.of("#와플", "#바나나"));

        doReturn(response).when(tagService).getTagsByQuestion(any(Long.class), any(Integer.class));

        //when
        ResultActions resultActions = this.mockMvc.perform(get(url, question.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + jwt)
        );

        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))
                .andExpect(jsonPath("$.result.tags").exists());
    }
}
