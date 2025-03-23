package com.server.capple.domain.member.repository;

import com.server.capple.domain.member.entity.AcademyGeneration;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.member.entity.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

import static com.server.capple.domain.member.entity.AcademyGeneration.GENERATION_3;
import static com.server.capple.domain.member.entity.Role.ROLE_ACADEMIER;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Member 레포지토리의 ")
@ActiveProfiles("test")
@Transactional
@SpringBootTest
class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    private static Stream<Arguments> provideEmailAndNullable() {
        return Stream.of(
            Arguments.of("qapple@qapple.net", true),
            Arguments.of("qsamsung@qsamsung.net", false)
        );
    }

    @ParameterizedTest(name = "[{index}] : {0}을 찾으면 존재한다 -> {1}")
    @MethodSource("provideEmailAndNullable")
    @DisplayName("qapple@qapple.net를 가진 회원만 존재할때")
    void getMemberByEmail(String email, Boolean isSavedMember) {
        // given
        String savedEmail = "qapple@qapple.net";
        String nickname = "qapple";
        String sub = "sub";
        Role role = ROLE_ACADEMIER;
        AcademyGeneration generation = GENERATION_3;

        Member savedMember = Member.builder()
            .nickname(nickname)
            .email(savedEmail)
            .sub(sub)
            .role(role)
            .academyGeneration(generation)
            .build();
        memberRepository.save(savedMember);

        // when
        Member memberByEmail = memberRepository.getMemberByEmail(email);

        // then
        if (isSavedMember) {
            assertThat(memberByEmail).extracting("nickname", "email", "sub", "role", "academyGeneration")
                .containsExactly(nickname, savedEmail, sub, role, generation);
        } else {
            assertThat(memberByEmail).isNull();
        }
    }
}