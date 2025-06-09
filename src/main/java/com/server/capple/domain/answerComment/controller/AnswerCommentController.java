package com.server.capple.domain.answerComment.controller;

import com.server.capple.config.security.AuthMember;
import com.server.capple.domain.answerComment.dto.AnswerCommentRequest;
import com.server.capple.domain.answerComment.dto.AnswerCommentResponse.*;
import com.server.capple.domain.answerComment.service.AnswerCommentService;
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

@Tag(name = "답변 댓글 API", description = "답변 댓글 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/answer-comments")
public class AnswerCommentController {

    private final AnswerCommentService answerCommentService;

    @Operation(summary = "답변 댓글 생성 API", description = " 답변 댓글 생성 API 입니다. pathvariable로 answerId를 주세요.")
    @PostMapping("/answer/{answerId}")
    public BaseResponse<AnswerCommentId> createAnswerComment(@AuthMember Member member, @PathVariable(value = "answerId") Long answerId, @RequestBody @Valid AnswerCommentRequest request) {
        return BaseResponse.onSuccess(answerCommentService.createAnswerComment(member, answerId, request));
    }

    @Operation(summary = "답변 댓글 삭제 API", description = " 답변 댓글 삭제 API 입니다. pathvariable 으로 commentId를 주세요.")
    @DeleteMapping("/{commentId}")
    public BaseResponse<AnswerCommentId> deleteAnswerComment(@AuthMember Member member, @PathVariable(value = "commentId") Long commentId) {
        return BaseResponse.onSuccess(answerCommentService.deleteAnswerComment(member, commentId));
    }

    @Operation(summary = "답변 댓글 수정 API", description = " 답변 댓글 수정 API 입니다. pathvariable 으로 commentId를 주세요.")
    @PatchMapping("/{commentId}")
    public BaseResponse<AnswerCommentId> updateAnswerComment(@AuthMember Member member, @PathVariable(value = "commentId") Long commentId, @RequestBody @Valid AnswerCommentRequest request) {
        return BaseResponse.onSuccess(answerCommentService.updateAnswerComment(member, commentId, request));
    }

    @Operation(summary = "답변 댓글 좋아요/취소 토글 API", description = " 답변 댓글 좋아요/취소 토글 API 입니다. pathvariable 으로 commentId를 주세요.")
    @PatchMapping("/heart/{commentId}")
    public BaseResponse<AnswerCommentLike> heartAnswerComment(@AuthMember Member member, @PathVariable(value = "commentId") Long commentId) {
        return BaseResponse.onSuccess(answerCommentService.toggleAnswerCommentHeart(member, commentId));
    }

    @Operation(summary = "답변에 대한 댓글 조회 API", description = " 답변에 대한 댓글 조회 API 입니다. pathvariable 으로 answerId를 주세요."
            + "pathVariable으로 answerId 주세요.<BR>**첫 번째 조회 시 threshold를 비워 보내고, 이후 조회 시 앞선 조회의 반환값으로 받은 threshold를 보내주세요.**")
    @GetMapping("/{answerId}")
    public BaseResponse<SliceResponse<AnswerCommentInfo>> getAnswerCommentInfos(
            @AuthMember Member member,
            @Parameter(description = "답변 식별자")
            @PathVariable(value = "answerId") Long answerId,
            @Parameter(description = "이전 조회의 마지막 index")
            @RequestParam(required = false, name = "threshold") Long lastIndex,
            @Parameter(description = "조회할 페이지 크기")
            @RequestParam(defaultValue = "1000", required = false) Integer pageSize) {
        return BaseResponse.onSuccess(answerCommentService.getAnswerCommentInfos(answerId, member.getId(), lastIndex,  PageRequest.of(0, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"))));
    }

}
