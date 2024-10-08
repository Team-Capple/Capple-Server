package com.server.capple.domain.answer.controller;

import com.server.capple.config.security.AuthMember;
import com.server.capple.domain.answer.dto.AnswerRequest;
import com.server.capple.domain.answer.dto.AnswerResponse;
import com.server.capple.domain.answer.dto.AnswerResponse.AnswerInfo;
import com.server.capple.domain.answer.dto.AnswerResponse.MemberAnswerInfo;
import com.server.capple.domain.answer.service.AnswerService;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.global.common.BaseResponse;
import com.server.capple.global.common.SliceResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
        + "pathVariable으로 questionId를 주세요.<BR>**첫 번째 조회 시 threshold를 비워 보내고, 이후 조회 시 앞선 조회의 반환값으로 받은 threshold를 보내주세요.**")
    @GetMapping("/question/{questionId}")
    public BaseResponse<SliceResponse<AnswerInfo>> getAnswerList(
        @AuthMember Member member,
        @Parameter(description = "질문 식별자")
        @PathVariable(value = "questionId") Long questionId,
        @Parameter(description = "이전 조회의 마지막 index")
        @RequestParam(required = false, name = "threshold") Long lastIndex,
        @Parameter(description = "조회할 페이지 크기")
        @RequestParam(defaultValue = "1000", required = false) Integer pageSize) {
        return BaseResponse.onSuccess(answerService.getAnswerList(member.getId(), questionId, lastIndex, PageRequest.of(0, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"))));
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

    @Operation(summary = "작성한 답변 조회 API", description = " 작성한 답변 조회 API 입니다.<BR>**첫 번째 조회 시 threshold를 비워 보내고, 이후 조회 시 앞선 조회의 반환값으로 받은 threshold를 보내주세요.**")
    @GetMapping
    public BaseResponse<SliceResponse<MemberAnswerInfo>> getMemberAnswer(
        @AuthMember Member member,
        @Parameter(description = "이전 조회의 마지막 index")
        @RequestParam(required = false, name = "threshold") Long lastIndex,
        @Parameter(description = "조회할 페이지 크기")
        @RequestParam(defaultValue = "1000", required = false) Integer pageSize) {
        return BaseResponse.onSuccess(answerService.getMemberAnswer(member, lastIndex, PageRequest.of(0, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"))));
    }

    @Operation(summary = "좋아한 답변 조회 API", description = " 좋아한 답변 조회 API 입니다.<BR>**첫 번째 조회 시 threshold를 비워 보내고, 이후 조회 시 앞선 조회의 반환값으로 받은 threshold를 보내주세요.**")
    @GetMapping("/heart")
    public BaseResponse<SliceResponse<MemberAnswerInfo>> getMemberHeartAnswer(
        @AuthMember Member member,
        @Parameter(description = "이전 조회의 마지막 index")
        @RequestParam(required = false, name = "threshold") Long lastIndex,
        @Parameter(description = "조회할 페이지 크기")
        @RequestParam(defaultValue = "1000", required = false) Integer pageSize) {
        return BaseResponse.onSuccess(answerService.getMemberHeartAnswer(member, lastIndex, PageRequest.of(0, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"))));
    }
}
