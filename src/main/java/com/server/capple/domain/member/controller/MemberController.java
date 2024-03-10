package com.server.capple.domain.member.controller;

import com.server.capple.domain.member.dto.MemberRequest;
import com.server.capple.domain.member.dto.MemberResponse;
import com.server.capple.domain.member.service.MemberService;
import com.server.capple.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @Operation(summary = "마이페이지 프로필 이미지 업로드 API", description = " 프로필 이미지 업로드 API 입니다." +
            " request part로 수정할 프로필 이미지를 주세요.")
    @PostMapping("/image")
    public BaseResponse<MemberResponse.ProfileImage> uploadImage(@RequestPart(value = "image") MultipartFile image) {
        return BaseResponse.onSuccess(memberService.uploadImage(image));
    }

    @Operation(summary = "마이페이지 프로필 수정 API", description = " 프로필 수정 API 입니다." +
            " request body로 수정할 멤버 id, 닉네임, 업로드한 이미지 url을 주세요.")
    @PostMapping("/{memberId}")
    public BaseResponse<MemberResponse.EditMemberInfo> editMemberInfo(@PathVariable Long memberId,
                                                                      @RequestBody @Valid MemberRequest.EditMemberInfo request) {
        return BaseResponse.onSuccess(memberService.editMemberInfo(memberId, request));
    }

    @Operation(summary = "로그인 API", description = "로그인 API 입니다. " +
        "쿼리 파라미터를 이용해 애플 인증서버에서 받아온 code를 입력해주세요." +
        "기존에 존재하는 사용자라면 isMember가 true로 반환되며, accessToken과 refreshToken이 반환됩니다." +
        "새로운 사용자라면 isMember가 false로 반환되며, refreshToken의 위치에 signUpToken이 반환됩니다.")
    @GetMapping("/sign-in")
    public BaseResponse<MemberResponse.SignInResponse> login(@RequestParam String code) {
        return BaseResponse.onSuccess(memberService.signIn(code));
    }
}