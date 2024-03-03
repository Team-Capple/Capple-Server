package com.server.capple.domain.question.controller;

import com.server.capple.domain.question.dto.response.QuestionResponse.QuestionSummary;
import com.server.capple.domain.question.entity.QuestionStatus;
import com.server.capple.domain.question.service.QuestionService;
import com.server.capple.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "질문 API", description = "질문 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/questions")
public class QuestionController {

    private final QuestionService questionService;

    @Operation(summary = "실시간 질문 조회 API", description = "실시간 질문을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
    })
    @GetMapping("/live")
    private BaseResponse<QuestionSummary> getLiveQuestionSummary() {
        return BaseResponse.onSuccess(questionService.getQuestionSummary(QuestionStatus.LIVE));
    }

    @Operation(summary = "최근 지난 질문 조회 API", description = "최근 지난 질문을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
    })
    @GetMapping("/lastOld")
    private BaseResponse<QuestionSummary> getLastOldQuestionSummary() {
        return BaseResponse.onSuccess(questionService.getQuestionSummary(QuestionStatus.OLD));
    }

}