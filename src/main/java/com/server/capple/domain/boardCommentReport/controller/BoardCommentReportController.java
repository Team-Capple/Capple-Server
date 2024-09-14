package com.server.capple.domain.boardCommentReport.controller;

import com.server.capple.config.security.AuthMember;
import com.server.capple.domain.boardCommentReport.dto.BoardCommentReportRequest;
import com.server.capple.domain.boardCommentReport.dto.BoardCommentReportResponse;
import com.server.capple.domain.boardCommentReport.service.BoardCommentReportService;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "게시판 댓글 신고 API", description = "게시판 댓글 신고 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/reports/board-comment")
public class BoardCommentReportController {
    private final BoardCommentReportService boardCommentReportService;

    @Operation(summary = "게시판 댓글 신고 API", description = "게시판 댓글 신고를 작성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
    })
    @PostMapping
    private BaseResponse<BoardCommentReportResponse.BoardCommentReportCreate> createBoardCommentReport(
            @AuthMember Member member,
            @RequestBody BoardCommentReportRequest.BoardCommentReportCreate request) {
        return BaseResponse.onSuccess(boardCommentReportService.createBoardCommentReport(member, request.getBoardCommentId(), request.getBoardCommentReportType()));
    }
}
