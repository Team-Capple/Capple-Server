package com.server.capple.domain.boardComment.controller;


import com.server.capple.config.security.AuthMember;
import com.server.capple.domain.boardComment.dto.BoardCommentRequest;
import com.server.capple.domain.boardComment.dto.BoardCommentResponse.ToggleBoardCommentHeart;
import com.server.capple.domain.boardComment.dto.BoardCommentResponse.BoardCommentId;
import com.server.capple.domain.boardComment.dto.BoardCommentResponse.BoardCommentInfos;
import com.server.capple.domain.boardComment.service.BoardCommentService;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "게시글 댓글 API", description = "게시글 댓글 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/board-comments")
public class BoardCommentController {

    private final BoardCommentService boardCommentService;

    @Operation(summary = "게시글 댓글 생성 API", description = " 게시글 댓글 생성 API 입니다. pathVariable 으로 boardId를 주세요.")
    @PostMapping("/board/{boardId}")
    public BaseResponse<BoardCommentId> createBoardComment(@AuthMember Member member,
                                                            @PathVariable(value = "boardId") Long boardId,
                                                            @RequestBody @Valid BoardCommentRequest request) {
        return BaseResponse.onSuccess(boardCommentService.createBoardComment(member, boardId, request));
    }

    @Operation(summary = "게시글 댓글 수정 API", description = " 게시글 댓글 수정 API 입니다. pathVariable 으로 commentId를 주세요.")
    @PatchMapping("/{commentId}")
    public BaseResponse<BoardCommentId> updateBoardComment(@AuthMember Member member, @PathVariable(value = "commentId") Long commentId, @RequestBody @Valid BoardCommentRequest request) {
        return BaseResponse.onSuccess(boardCommentService.updateBoardComment(member, commentId, request));
    }

    @Operation(summary = "게시글 댓글 삭제 API", description = " 게시글 댓글 삭제 API 입니다. pathVariable 으로 commentId를 주세요.")
    @DeleteMapping("/{commentId}")
    public BaseResponse<BoardCommentId> deleteBoardComment(@AuthMember Member member, @PathVariable(value = "commentId") Long commentId) {
        return BaseResponse.onSuccess(boardCommentService.deleteBoardComment(member, commentId));
    }


    @Operation(summary = "게시글 댓글 좋아요/취소 토글 API", description = " 게시글 댓글 좋아요/취소 토글 API 입니다. pathVariable 으로 commentId를 주세요.")
    @PatchMapping("/heart/{commentId}")
    public BaseResponse<ToggleBoardCommentHeart> heartBoardComment(@AuthMember Member member, @PathVariable(value = "commentId") Long commentId) {
        return BaseResponse.onSuccess(boardCommentService.toggleBoardCommentHeart(member, commentId));
    }

    @Operation(summary = "게시글 댓글 리스트 조회 API", description = " 게시글 댓글 리스트 조회 API 입니다. pathVariable 으로 boardId를 주세요.")
    @GetMapping("/{boardId}")
    public BaseResponse<BoardCommentInfos> getBoardCommentInfos(@AuthMember Member member, @PathVariable(value = "boardId") Long boardId) {
        return BaseResponse.onSuccess(boardCommentService.getBoardCommentInfos(member,boardId));
    }

}
