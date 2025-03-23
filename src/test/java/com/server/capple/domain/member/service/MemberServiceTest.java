package com.server.capple.domain.member.service;

import com.server.capple.config.security.jwt.service.JwtService;
import com.server.capple.domain.member.dto.MemberRequest;
import com.server.capple.domain.member.dto.MemberResponse;
import com.server.capple.domain.member.entity.AcademyGeneration;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.member.entity.Role;
import com.server.capple.support.ServiceTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static com.server.capple.domain.member.entity.AcademyGeneration.GENERATION_3;
import static com.server.capple.domain.member.entity.Role.ROLE_ACADEMIER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Member 서비스의 ")
@SpringBootTest
public class MemberServiceTest extends ServiceTestConfig {

    @Autowired MemberService memberService;
    @Autowired
    JwtService jwtService;

    @Test
    @DisplayName("Member 프로필 조회 테스트")
    @Transactional
    public void getMyPageMemberInfo() {
        //given & when
        MemberResponse.MyPageMemberInfo memberInfo = memberService.getMemberInfo(member);

        //then
        assertEquals(memberInfo.getNickname(), member.getNickname());
        assertEquals(memberInfo.getProfileImage(), member.getProfileImage());
    }

    @Test
    @DisplayName("프로필 수정 테스트")
    @Transactional
    public void editProfileTest() {
        //given
        MemberRequest.EditMemberInfo request = MemberRequest.EditMemberInfo.builder()
                .nickname("아리")
                // TODO : 추후 삭제
                .profileImage("")
//                .profileImage("ari.png")
                .build();

        //when
        MemberResponse.EditMemberInfo memberInfo = memberService.editMemberInfo(member, request);

        //then
        assertEquals(memberInfo.getNickname(), "아리");
//        assertEquals(memberInfo.getProfileImage(), "ari.png");
        // TODO : 추후 삭제
        assertEquals(memberInfo.getProfileImage(), "");
    }

    @Test
    @Transactional
    @DisplayName("회원가입 시 access토큰과 refresh 토큰을 생성한다.")
    void signUp() {
        // given
        String email = "qapple@qapple.net";
        String nickname = "qapple";
        String sub = "sub";
        String deviceToken = "deviceToken";
        String signUpAccessJwt = jwtService.createSignUpAccessJwt(sub);

        // when
        MemberResponse.Tokens tokens = memberService.signUp(signUpAccessJwt, email, nickname, "", deviceToken);

        // then
        assertThat(jwtService.getMemberId(tokens.getAccessToken()))
            .isEqualTo(jwtService.getMemberId(tokens.getRefreshToken()));
        assertThat(tokens.getAccessToken()).isNotNull();
        assertThat(tokens.getRefreshToken()).isNotNull();
    }

    @Test
    @Transactional
    @DisplayName("동일한 email로 회원가입을 시도할경우 sub만 덮어써진다.")
    void signUpWithDuplicateEmail() {
        // given
        String email = "qapple@qapple.net";
        String encryptedEmail = jwtService.createJwtFromEmail(email);
        String nickname = "qapple";
        String sub = "sub";
        Role role = ROLE_ACADEMIER;
        AcademyGeneration generation = GENERATION_3;
        String deviceToken = "deviceToken";

        Member buildedMember = Member.builder()
            .nickname(nickname)
            .email(encryptedEmail)
            .sub(sub)
            .role(role)
            .academyGeneration(generation)
            .build();
        Member savedMember = memberRepository.save(buildedMember);

        String newSub = "newSub";
        String signUpAccessJwt = jwtService.createSignUpAccessJwt(newSub);

        // when
        MemberResponse.Tokens tokens = memberService.signUp(signUpAccessJwt, email, nickname, "", deviceToken);

        // then
        assertThat(memberRepository.getMemberByEmail(encryptedEmail).getSub()).isEqualTo(newSub);
        assertThat(jwtService.getMemberId(tokens.getAccessToken())).isEqualTo(savedMember.getId());
        assertThat(jwtService.getMemberId(tokens.getRefreshToken())).isEqualTo(savedMember.getId());
        assertThat(tokens.getAccessToken()).isNotNull();
        assertThat(tokens.getRefreshToken()).isNotNull();
    }
}
