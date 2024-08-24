package com.server.capple.domain.answerComment.controller;

import com.server.capple.config.security.AuthMember;
import com.server.capple.domain.answerComment.dto.AnswerCommentRequest;
import com.server.capple.domain.answerComment.dto.AnswerCommentResponse.*;
import com.server.capple.domain.answerComment.service.AnswerCommentService;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "답변 댓글 API", description = "답변 댓글 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/answerComments")
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
    public BaseResponse<AnswerCommentHeart> heartAnswerComment(@AuthMember Member member, @PathVariable(value = "commentId") Long commentId) {
        return BaseResponse.onSuccess(answerCommentService.heartAnswerComment(member, commentId));
    }

    @Operation(summary = "답변에 대한 댓글 조회 API", description = " 답변에 대한 댓글 조회 API 입니다. pathvariable 으로 answerId를 주세요.")
    @GetMapping("/{answerId}")
    public BaseResponse<AnswerCommentInfos> getAnswerCommentInfos(@PathVariable(value = "answerId") Long answerId) {
        return BaseResponse.onSuccess(answerCommentService.getAnswerCommentInfos(answerId));
    }

}
