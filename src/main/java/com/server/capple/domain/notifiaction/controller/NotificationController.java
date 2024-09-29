package com.server.capple.domain.notifiaction.controller;

import com.server.capple.config.security.AuthMember;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.notifiaction.dto.NotificationResponse.NotificationInfo;
import com.server.capple.domain.notifiaction.service.NotificationService;
import com.server.capple.global.common.BaseResponse;
import com.server.capple.global.common.SliceResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Tag(name = "알림 API", description = "알림 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    @Operation(summary = "알림 리스트 조회 API", description = "API를 호출한 사용자가 받은 알림 리스트를 조회합니다. 알림은 최신순으로 정렬되어 반환됩니다.")
    @GetMapping
    public BaseResponse<SliceResponse<NotificationInfo>> getNotifications(
        @AuthMember Member member,
        @Parameter(description = "조회 기준 시각")
        @RequestParam(required = false) LocalDateTime thresholdDate,
        @RequestParam(defaultValue = "0", required = false) Integer pageNumber, @RequestParam(defaultValue = "1000", required = false) Integer pageSize) {
        return BaseResponse.onSuccess(notificationService.getNotifications(member, thresholdDate, PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"))));
    }
}
