package com.server.capple.domain.question.controller;

import com.server.capple.domain.question.dto.request.QuestionRequest.QuestionCreate;
import com.server.capple.domain.question.dto.response.QuestionResponse.QuestionId;
import com.server.capple.domain.question.service.AdminQuestionService;
import com.server.capple.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "관리자 질문 API", description = "관리자 질문 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/questions")
public class AdminQuestionController {
    private final AdminQuestionService adminQuestionService;

    @Operation(summary = "질문 추가 API", description = "질문을 추가합니다. content를 입력하세요")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "COMMON200", description = "성공"),
    })
    @PostMapping
    public BaseResponse<QuestionId> createQuestion(
        @RequestBody QuestionCreate request) {

        return BaseResponse.onSuccess(adminQuestionService.createQuestion(request));
    }

    @Operation(summary = "질문 삭제 API", description = "질문을 삭제합니다. 삭제할 질문의 ID를 입력하세요")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "COMMON200", description = "성공,")
    })
    @DeleteMapping("/{questionId}")
    public BaseResponse<QuestionId> deleteQuestion(
        @PathVariable(value = "questionId") Long questionId) {

        return BaseResponse.onSuccess(adminQuestionService.deleteQuestion(questionId));
    }

    @PostMapping(value = "/upload/csv", consumes = "text/csv")
    public BaseResponse<Long> uploadQuestionByCsv(@RequestBody String text) {
        return BaseResponse.onSuccess(adminQuestionService.uploadQuestionByCsv(text));
    }
}
