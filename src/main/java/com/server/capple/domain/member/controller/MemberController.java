package com.server.capple.domain.member.controller;

import com.server.capple.domain.member.dto.response.MemberResponse;
import com.server.capple.domain.member.service.MemberService;
import com.server.capple.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
@Tag(name = "멤버 API", description = "멤버 API입니다.")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "마이페이지 프로필 조회 API", description = " 프로필 조회 API 입니다." +
            " path variable로 조회할 memberId를 주세요.")
    @GetMapping("/{memberId}")
    public BaseResponse<MemberResponse.MyPageMemberInfo> getMemberInfo(@PathVariable Long memberId) {
        return BaseResponse.onSuccess(memberService.getMemberInfo(memberId));
    }

}
