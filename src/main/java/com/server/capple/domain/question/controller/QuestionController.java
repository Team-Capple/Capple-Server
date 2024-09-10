package com.server.capple.domain.question.controller;

import com.server.capple.config.security.AuthMember;
import com.server.capple.domain.board.dto.BoardResponse;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.question.dto.response.QuestionResponse;
import com.server.capple.domain.question.dto.response.QuestionResponse.QuestionSummary;
import com.server.capple.domain.question.dto.response.QuestionResponse.QuestionInfos;
import com.server.capple.domain.question.service.QuestionService;
import com.server.capple.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "모든 질문 조회 API", description = "모든 질문을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
    })
    @GetMapping
    private BaseResponse<QuestionInfos> getQuestions(@AuthMember Member member) {
        return BaseResponse.onSuccess(questionService.getQuestions(member));
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
