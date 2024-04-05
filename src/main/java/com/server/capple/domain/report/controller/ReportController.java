package com.server.capple.domain.report.controller;

import com.server.capple.config.security.AuthMember;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.report.dto.reponse.ReportResponse;
import com.server.capple.domain.report.dto.request.ReportRequest;
import com.server.capple.domain.report.service.ReportService;
import com.server.capple.global.common.BaseEntity;
import com.server.capple.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "신고 API", description = "신고 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    @Operation(summary = "신고함 작성 API", description = "신고를 작성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
    })
    @PostMapping
    private BaseResponse<ReportResponse.ReportId> createReport(@AuthMember Member member, @RequestBody ReportRequest.ReportCreate request) {
        return BaseResponse.onSuccess(reportService.createReport(member, request));
    }

    @Operation(summary = "신고함 조회 API", description = "신고함을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
    })
    @GetMapping
    private BaseResponse<ReportResponse.ReportInfos> getReports(@AuthMember Member member) {
        return BaseResponse.onSuccess(reportService.getReports(member));
    }
}
