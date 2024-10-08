package com.server.capple.domain.boardComment.controller;


import com.server.capple.config.security.AuthMember;
import com.server.capple.domain.boardComment.dto.BoardCommentRequest;
import com.server.capple.domain.boardComment.dto.BoardCommentResponse.BoardCommentId;
import com.server.capple.domain.boardComment.dto.BoardCommentResponse.BoardCommentInfo;
import com.server.capple.domain.boardComment.dto.BoardCommentResponse.ToggleBoardCommentHeart;
import com.server.capple.domain.boardComment.service.BoardCommentService;
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

    @Operation(summary = "게시글 댓글 리스트 조회 API", description = " 게시글 댓글 리스트 조회 API 입니다. pathVariable 으로 boardId를 주세요.<BR>**첫 번째 조회 시 threshold를 비워 보내고, 이후 조회 시 앞선 조회의 반환값으로 받은 threshold를 보내주세요.**")
    @GetMapping("/{boardId}")
    public BaseResponse<SliceResponse<BoardCommentInfo>> getBoardCommentInfos(@AuthMember Member member, @PathVariable(value = "boardId") Long boardId,
                                                                              @Parameter(description = "이전 조회의 마지막 index")
                                                                              @RequestParam(required = false, name = "threshold") Long lastIndex,
                                                                              @RequestParam(defaultValue = "1000", required = false) Integer pageSize) {
        return BaseResponse.onSuccess(boardCommentService.getBoardCommentInfos(member,boardId, lastIndex, PageRequest.of(0,pageSize, Sort.by(Sort.Direction.ASC, "createdAt"))));
    }

}
