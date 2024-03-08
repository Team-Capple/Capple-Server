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

    @Operation(summary = "미사용 이미지 삭제 API", description = " 미사용 이미지 API 입니다.")
    @DeleteMapping("/image")
    public BaseResponse<MemberResponse.DeleteProfileImages> deleteOrphanageImages() {
        return BaseResponse.onSuccess(memberService.deleteOrphanageImages());
    }
}
