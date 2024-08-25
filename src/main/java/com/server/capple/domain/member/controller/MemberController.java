package com.server.capple.domain.member.controller;

import com.server.capple.config.security.AuthMember;
import com.server.capple.domain.member.dto.MemberRequest;
import com.server.capple.domain.member.dto.MemberResponse;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.member.entity.Role;
import com.server.capple.domain.member.service.MemberService;
import com.server.capple.global.common.BaseResponse;
import com.server.capple.global.exception.errorCode.AppleOauthError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @GetMapping("/mypage")
    public BaseResponse<MemberResponse.MyPageMemberInfo> getMemberInfo(@AuthMember Member member) {
        return BaseResponse.onSuccess(memberService.getMemberInfo(member));
    }

    @Operation(summary = "마이페이지 프로필 이미지 업로드 API", description = " 프로필 이미지 업로드 API 입니다." +
            " request part로 수정할 프로필 이미지를 주세요.")
    @PostMapping("/image")
    public BaseResponse<MemberResponse.ProfileImage> uploadImage(@RequestPart(value = "image") MultipartFile image) {
        return BaseResponse.onSuccess(memberService.uploadImage(image));
    }

    @Operation(summary = "마이페이지 프로필 수정 API", description = " 프로필 수정 API 입니다." +
            " request body로 수정할 멤버 id, 닉네임, 업로드한 이미지 url을 주세요.")
    @PostMapping("/mypage")
    public BaseResponse<MemberResponse.EditMemberInfo> editMemberInfo(@AuthMember Member member,
                                                                      @RequestBody @Valid MemberRequest.EditMemberInfo request) {
        return BaseResponse.onSuccess(memberService.editMemberInfo(member, request));
    }

    @Operation(summary = "미사용 이미지 삭제 API", description = " 미사용 이미지 API 입니다.")
    @DeleteMapping("/image")
    public BaseResponse<MemberResponse.DeleteProfileImages> deleteOrphanageImages() {
        return BaseResponse.onSuccess(memberService.deleteOrphanageImages());
    }

    @Operation(summary = "로그인 API", description = "로그인 API 입니다. " +
            "쿼리 파라미터를 이용해 애플 인증서버에서 받아온 code를 입력해주세요." +
            "기존에 존재하는 사용자라면 isMember가 true로 반환되며, accessToken과 refreshToken이 반환됩니다." +
            "새로운 사용자라면 isMember가 false로 반환되며, refreshToken의 위치에 signUpToken이 반환됩니다.")
    @GetMapping("/sign-in")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "회원 검증 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MemberResponse.SignInResponse.class))),
        @ApiResponse(responseCode = "400", description = "로그인 데이터/형식 오류", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "403", description = "인증서버 접근 권한 오류", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "서버 내 인증서버 클라이언트 오류", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "애플 인증서버 내부 오류", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppleOauthError.class), examples = {
            @ExampleObject(name = "OAUTH005", value = """
                {
                  "timeStamp": "2024-04-05T07:55:47.289412",
                  "code": "OAUTH005",
                  "message": "애플 인증서버 내부 오류입니다."
                }
                """, description = "애플 인증서버측의 오류입니다. 다시 시도해주세요."),
            @ExampleObject(name = "GLOBAL005", value = """
                {
                  "timeStamp": "2024-04-05T07:55:47.289412",
                    "code": "GLOBAL005",
                    "message": "서버 에러, 관리자에게 문의해주세요."
                }
                """, description = "서버측의 애플 서버 접근 클라언트 관련 문제로 서버 관리자에게 문의해주세요."),
        })),
    })
    public BaseResponse<MemberResponse.SignInResponse> login(@RequestParam String code, @RequestParam String deviceToken) {
        return BaseResponse.onSuccess(memberService.signIn(code, deviceToken));
    }

    @Operation(summary = "회원가입 API", description = "회원가입 API 입니다." +
            "로컬 로그인 또는 소셜 로그인으로 생성한 token을 이용해 회원가입을 진행합니다." +
            "body에 로그인 실패시 반환 받은 signUpToken, 이메일, 닉네임, 프로필 이미지를 입력해주세요. 프로필 이미지는 필수가 아닙니다." +
            "회원가입 성공시 accessToken과 refreshToken이 반환됩니다.")
    @PostMapping("/sign-up")
    public BaseResponse<MemberResponse.Tokens> signUp(@RequestBody MemberRequest.signUp request) {
        return BaseResponse.onSuccess(memberService.signUp(request.getSignUpToken(), request.getEmail(), request.getNickname(), request.getProfileImage(), request.getDeviceToken()));
    }

    @Operation(summary = "테스트용 로컬 로그인 API", description = "테스트용 로컬 로그인 API 입니다." +
            "쿼리 파라미터를 이용해 테스트용 아이디를 입력해주세요." +
            "refreshToken의 위치에 signUpToken이 반환됩니다.")
    @GetMapping("/local-sign-in")
    public BaseResponse<MemberResponse.SignInResponse> localLogin(@RequestParam String testId, @RequestParam String deviceToken) {
        return BaseResponse.onSuccess(memberService.localSignIn(testId, deviceToken));
    }

    @Operation(summary = "회원탈퇴 API", description = "회원탈퇴 API 입니다.")
    @GetMapping("/resign")
    public BaseResponse<MemberResponse.MemberId> resignMember(@AuthMember Member member) {
        return BaseResponse.onSuccess(memberService.resignMember(member));
    }

    @Operation(summary = "사용자 자격 변경", description = "사용자 자격 변경 API 입니다." +
        "쿼리 파라미터를 이용해 변경할 role을 입력해주세요." +
        "Role은 \"ROLE_ACADEMIER\", \"ROLE_ADMIN\" 중 하나로 입력해주세요.")
    @GetMapping("/role/change")
    public BaseResponse<MemberResponse.Tokens> changeRole(@AuthMember Member member, Role role) {
        return BaseResponse.onSuccess(memberService.changeRole(member.getId(), role));
    }

    @Operation(summary = "닉네임 중복 체크", description = "닉네임 중복 체크 API 입니다." +
        "쿼리 파라미터를 이용해 닉네임을 입력해주세요." +
        "중복 닉네임일 경우 true, 중복되지 않은 닉네임일 경우 false가 반환됩니다.")
    @GetMapping("/nickname/check")
    public BaseResponse<Boolean> checkNickname(@RequestParam String nickname) {
        return BaseResponse.onSuccess(memberService.checkNickname(nickname));
    }

    @Operation(summary = "이메일 중복 체크", description = "이메일 중복 체크 API 입니다." +
        "쿼리 파라미터를 이용해 이메일을 입력해주세요." +
        "중복 이메일일 경우 true, 중복되지 않은 이메일일 경우 false가 반환됩니다.")
    @GetMapping("/email/check")
    public BaseResponse<Boolean> checkEmail(@RequestParam String email) {
        return BaseResponse.onSuccess(memberService.checkEmail(email));
    }

    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "메일 발송 성공"),
        @ApiResponse(responseCode = "400", description = "메일 발송 실패")
    })
    @Operation(summary = "회원가입 인증메일 발송 API", description = "회원가입 인증메일 발송 API 입니다.<br>" +
        "쿼리 파라미터를 이용해 이메일을 입력해주세요.<br>" +
        "인증코드가 발송됩니다.")
    @GetMapping("/email/certification")
    public BaseResponse<Boolean> sendCertMail(
        @Parameter(description = "회원가입 인증 토큰")
        @RequestParam String signUpToken,
        @Parameter(description = "인증 메일을 받을 이메일 주소")
        @RequestParam String email
    ) {
        return BaseResponse.onSuccess(memberService.sendCertMail(signUpToken, email));
    }

    @Operation(summary = "회원가입 인증코드 인증 API", description = "회원가입 인증코드 인증API 입니다.<br>" +
        "쿼리 파라미터를 이용해 이메일과 인증코드를 입력해주세요.<br>" +
        "인증코드가 일치할 경우 true, 일치하지 않을 경우 false가 반환됩니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "메일 인증 성공"),
        @ApiResponse(responseCode = "400", description = "메일 인증 실패")
    })
    @GetMapping("/email/certification/check")
    public BaseResponse<Boolean> checkCertCode(
        @Parameter(description = "회원가입 인증 토큰")
        @RequestParam String signUpToken,
        @Parameter(description = "인증 메일을 받은 이메일 주소")
        @RequestParam String email,
        @Parameter(description = "인증 코드")
        @RequestParam String certCode
    ) {
        return BaseResponse.onSuccess(memberService.checkCertCode(signUpToken, email, certCode));
    }

    @Operation(summary = "메일 인증 화이트 리스트 등록 API", description = "메일 인증 화이트 리스트 등록 API 입니다.<br>" +
        "쿼리 파라미터를 이용해 화이트 리스트에 등록할 이메일 주소와 등록 기간(분)을 입역해주세요.<br>" +
        "등록에 성공할 경우 true가 반환됩니다.<br>" +
        "관리자 권한을 가진 토큰을 이용해야 해당 API를 사용할 수 있습니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "화이트 리스트 등록 성공"),
        @ApiResponse(responseCode = "403", description = "화이트 리스트 등록 실패, 관리자 권한 필요."),
    })
    @GetMapping("/email/whitelist/register")
    public BaseResponse<Boolean> registerEmailWhitelist(
        @Parameter(description = "화이트 리스트 등록할 이메일")
        @RequestParam String mail,
        @Parameter(description = "화이트 리스트 등록 기간 (분)")
        @RequestParam Long whitelistDurationMinutes) {
        return BaseResponse.onSuccess(memberService.registerEmailWhitelist(mail, whitelistDurationMinutes));
    }

    @Operation(summary = "로그아웃 API", description = "로그아웃 API 입니다.<br>저장된 디바이스 토큰을 지웁니다.")
    @GetMapping("/logout")
    public BaseResponse<Boolean> logout(@AuthMember Member member) {
        return BaseResponse.onSuccess(memberService.logout(member));
    }
}
