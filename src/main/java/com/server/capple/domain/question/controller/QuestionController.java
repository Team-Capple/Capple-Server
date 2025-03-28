package com.server.capple.domain.question.controller;

import com.server.capple.config.security.AuthMember;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.question.dto.response.QuestionResponse;
import com.server.capple.domain.question.dto.response.QuestionResponse.QuestionInfo;
import com.server.capple.domain.question.dto.response.QuestionResponse.QuestionSummary;
import com.server.capple.domain.question.service.QuestionService;
import com.server.capple.global.common.BaseResponse;
import com.server.capple.global.common.SliceResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Tag(name = "질문 API", description = "질문 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/questions")
public class QuestionController {

    private final QuestionService questionService;

    @Operation(summary = "메인 질문 조회 API", description = "메인 질문을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "COMMON200", description = "성공"),
    })
    @GetMapping("/main")
    private BaseResponse<QuestionSummary> getMainQuestion(@AuthMember Member member) {
        return BaseResponse.onSuccess(questionService.getMainQuestion(member));
    }

    @Operation(summary = "모든 질문 조회 API", description = "모든 질문을 조회합니다.<BR>**첫 번째 조회 시 threshold를 비워 보내고, 이후 조회 시 앞선 조회의 반환값으로 받은 threshold를 보내주세요.**")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "COMMON200", description = "성공"),
    })
    @GetMapping
    private BaseResponse<SliceResponse<QuestionInfo>> getQuestions(
        @AuthMember Member member,
        @Parameter(description = "이전 조회의 마지막 데이터의 시각")
        @RequestParam(required = false, name = "threshold") LocalDateTime lastDateTime,
        @RequestParam(defaultValue = "1000", required = false) Integer pageSize
        ) {
        return BaseResponse.onSuccess(questionService.getQuestions(member, lastDateTime, PageRequest.of(0, pageSize, Sort.by(Sort.Direction.DESC, "livedAt"))));
    }

    @Operation(summary = "사용자가 답변한 질문 리스트 조회")
    @GetMapping("/answered")
    public BaseResponse<SliceResponse<QuestionInfo>> getAnsweredQuestion(
        @AuthMember Member member,
        @Parameter(description = "이전 조회의 마지막 데이터의 시각")
        @RequestParam(required = false, name = "threshold") LocalDateTime lastDateTime,
        @RequestParam(defaultValue = "1000", required = false) Integer pageSize
    ) {
        return BaseResponse.onSuccess(questionService.getAnsweredQuestions(member, lastDateTime, PageRequest.of(0, pageSize, Sort.by(Sort.Direction.DESC, "livedAt"))));
    }

    @Operation(summary = "사용자가 답변하지 않은 질문 리스트 조회")
    @GetMapping("/notAnswered")
    public BaseResponse<SliceResponse<QuestionInfo>> getNotAnsweredQuestion(
        @AuthMember Member member,
        @Parameter(description = "이전 조회의 마지막 데이터의 시각")
        @RequestParam(required = false, name = "threshold") LocalDateTime lastDateTime,
        @RequestParam(defaultValue = "1000", required = false) Integer pageSize
    ) {
        return BaseResponse.onSuccess(questionService.getNotAnsweredQuestions(member, lastDateTime, PageRequest.of(0, pageSize, Sort.by(Sort.Direction.DESC, "livedAt"))));
    }

    @Operation(summary = "질문 좋아요/취소 API", description = " 질문 좋아요/취소 API 입니다." +
        "pathvariable 으로 questionId를 주세요.")
    @PostMapping("/{questionId}/heart")
    public BaseResponse<QuestionResponse.QuestionToggleHeart> toggleBoardHeart(@AuthMember Member member, @PathVariable(value = "questionId") Long questionId) {
        return BaseResponse.onSuccess(questionService.toggleQuestionHeart(member, questionId));
    }


//    @Operation(summary = "최근 지난 질문 조회 API", description = "최근 지난 질문을 조회합니다.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "COMMON200", description = "성공"),
//    })
//    @GetMapping("/lastOld")
//    private BaseResponse<QuestionSummary> getLastOldQuestionSummary() {
//        return BaseResponse.onSuccess(questionService.getQuestionSummary(QuestionStatus.OLD));
//    }

}
