package com.server.capple.domain.answer.controller;

import com.server.capple.config.security.AuthMember;
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
import org.springframework.data.domain.PageRequest;
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
    public BaseResponse<AnswerResponse.AnswerId> createAnswer(@AuthMember Member member, @PathVariable(value = "questionId") Long questionId,
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
            @AuthMember Member member,
            @PathVariable(value = "questionId") Long questionId,
            @RequestParam(name = "keyword", required = false) String keyword,
            @PageableDefault
            @Parameter(hidden = true) Pageable pageable) {
        Pageable tempPageable = PageRequest.of(pageable.getPageNumber(), 250);
        return BaseResponse.onSuccess(answerService.getAnswerList(member.getId(), questionId, keyword, tempPageable));
    }

    @Operation(summary = "답변 수정 API", description = " 답변 수정 API 입니다." +
            "pathvariable 으로 answerId를 주세요.")
    @PatchMapping("/{answerId}")
    public BaseResponse<AnswerResponse.AnswerId> updateAnswer(@AuthMember Member member, @PathVariable(value = "answerId") Long answerId,
                                                              @RequestBody @Valid AnswerRequest request) {
        return BaseResponse.onSuccess(answerService.updateAnswer(member, answerId, request));
    }

    @Operation(summary = "답변 삭제 API", description = " 답변 삭제 API 입니다." +
            "pathvariable 으로 answerId를 주세요.")
    @DeleteMapping("/{answerId}")
    public BaseResponse<AnswerResponse.AnswerId> deleteAnswer(@AuthMember Member member, @PathVariable(value = "answerId") Long answerId) {
        return BaseResponse.onSuccess(answerService.deleteAnswer(member, answerId));
    }

    @Operation(summary = "답변 좋아요/취소 API", description = " 답변 좋아요/취소 API 입니다." +
            "pathvariable 으로 answerId를 주세요.")
    @PostMapping("/{answerId}/heart")
    public BaseResponse<AnswerResponse.AnswerLike> toggleAnswerHeart(@AuthMember Member member, @PathVariable(value = "answerId") Long answerId) {
        return BaseResponse.onSuccess(answerService.toggleAnswerHeart(member, answerId));
    }

    @Operation(summary = "작성한 답변 조회 API", description = " 작성한 답변 조회 API 입니다.")
    @GetMapping
    public BaseResponse<AnswerResponse.MemberAnswerList> getMemberAnswer(@AuthMember Member member) {
        return BaseResponse.onSuccess(answerService.getMemberAnswer(member));
    }

    @Operation(summary = "좋아한 답변 조회 API", description = " 좋아한 답변 조회 API 입니다.")
    @GetMapping("/heart")
    public BaseResponse<AnswerResponse.MemberAnswerList> getMemberHeartAnswer(@AuthMember Member member) {
        return BaseResponse.onSuccess(answerService.getMemberHeartAnswer(member));
    }
}
