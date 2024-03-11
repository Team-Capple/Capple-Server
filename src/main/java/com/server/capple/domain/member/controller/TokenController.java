package com.server.capple.domain.member.controller;

import com.server.capple.config.security.AuthMember;
import com.server.capple.config.security.jwt.service.JwtService;
import com.server.capple.domain.member.dto.MemberResponse;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "토큰 API", description = "토큰 API")
@RequestMapping("/token")
public class TokenController {
    private final JwtService jwtService;

    @Operation(summary = "토큰 재발급 API", description = "토큰 재발급 API 입니다.")
    @GetMapping("/refresh")
    public BaseResponse<MemberResponse.Tokens> refreshTokens(@AuthMember Member member) {
        return BaseResponse.onSuccess(jwtService.refreshTokens(member.getId(), member.getRole()));
    }
}
