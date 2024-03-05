package com.server.capple.domain.answer.controller;

import com.server.capple.domain.answer.dto.AnswerRequest;
import com.server.capple.domain.answer.dto.AnswerResponse;
import com.server.capple.domain.answer.service.AnswerService;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@Tag(name = "답변 API", description = "답변 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/answers")
public class AnswerController {

    private final AnswerService answerService;

    @Operation(summary = "답변 생성 API", description = " 답변 생성 API 입니다." +
            "pathvariable 으로 questionId를 주세요.")
    @PostMapping("/question/{questionId}")
    public BaseResponse<AnswerResponse.AnswerId> createAnswer(Member member, @PathVariable(value = "questionId") Long questionId,
                                                              @RequestBody @Valid AnswerRequest request) {
        return BaseResponse.onSuccess(answerService.createAnswer(member, questionId, request));
    }

    @Operation(summary = "질문에 대한 답변 조회 API", description = "특정 질문에 대한 답변리스트를 조회하는 API입니다."
            + "pathVariable으로 questionId를 주세요."
            + "조회할 질문의 개수를 param으로 입력해주세요.")
    @Parameters(value = {
            @Parameter(name = "keyword", description = "검색"),
            @Parameter(name = "size", description = "조회할 질문의 개수를 입력하세요."),
    })
    @GetMapping("/question/{questionId}")
    public BaseResponse<AnswerResponse.AnswerList> getAnswerList(
            @PathVariable(value = "questionId") Long questionId,
            @RequestParam(name = "keyword", required = false) String keyword,
            @PageableDefault
            @Parameter(hidden = true) Pageable pageable) {
        return BaseResponse.onSuccess(answerService.getAnswerList(questionId, keyword, pageable));
    }
}
