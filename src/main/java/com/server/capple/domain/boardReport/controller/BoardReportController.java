package com.server.capple.domain.boardReport.controller;

import com.server.capple.config.security.AuthMember;
import com.server.capple.domain.boardReport.dto.BoardReportRequest;
import com.server.capple.domain.boardReport.dto.BoardReportResponse;
import com.server.capple.domain.boardReport.service.BoardReportService;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.report.dto.reponse.ReportResponse;
import com.server.capple.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "게시판 신고 API", description = "게시판 신고 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/board-reports")
public class BoardReportController {

    private final BoardReportService boardReportService;

    @Operation(summary = "게시판 신고함 작성 API", description = "게시판 신고를 작성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
    })
    @PostMapping
    private BaseResponse<BoardReportResponse.BoardReportCreate> createBoardReport(
            @AuthMember Member member,
            @RequestBody BoardReportRequest.BoardReportCreate request) {
        return BaseResponse.onSuccess(boardReportService.createBoardReport(member, request.getBoardReportType()));
    }

    @Operation(summary = "게시판 신고함 조회 API", description = "게시판 신고함을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
    })
    @GetMapping
    private BaseResponse<BoardReportResponse.BoardReportsGet> getBoardReports(
            @AuthMember Member member) {
        return BaseResponse.onSuccess(boardReportService.getBoardReports(member));
    }

    @Operation(summary = "게시판 신고함 수정 API", description = "게시판 신고함을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
    })
    @PostMapping("/{boardReportId}")
    private BaseResponse<BoardReportResponse.BoardReportUpdate> updateBoardReport(
            @AuthMember Member member,
            @PathVariable(value = "boardReportId") Long reportId,
            @RequestBody @Valid BoardReportRequest.BoardReportUpdate request) {

        return BaseResponse.onSuccess(boardReportService.updateBoardReport(member, reportId, request.getBoardReportType()));
    }

    @Operation(summary = "게시판 신고함 삭제 API", description = "게시판 신고를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
    })
    @DeleteMapping("/{boardReportId}")
    private BaseResponse<BoardReportResponse.BoardReportResign> resignBoardReport(
            @AuthMember Member member,
            @PathVariable(value = "boardReportId") Long boardReportId) {

        return BaseResponse.onSuccess(boardReportService.resignBoardReport(member, boardReportId));
    }
}