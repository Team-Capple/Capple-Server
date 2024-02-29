package com.server.capple.domain.member.controller;

import com.server.capple.domain.member.dto.response.MemberInfoRes;
import com.server.capple.domain.member.service.MemberService;
import com.server.capple.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    /**
     * todo: 로그인 구현 완료 시 수정 해야함
     * 마이페이지 프로필 조회
     * @param memberId 조회할 멤버 id
     * @return 멤버 프로필 조회 내용
     */
    @GetMapping
    public BaseResponse<MemberInfoRes> getMemberInfo(@PathVariable Long memberId) {
        return BaseResponse.onSuccess(memberService.getMemberInfo(memberId));
    }

}
