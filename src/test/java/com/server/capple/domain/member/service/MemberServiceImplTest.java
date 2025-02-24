package com.server.capple.domain.member.service;

import com.server.capple.domain.member.entity.AcademyGeneration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("MemberImpl 서비스의 ")
@ActiveProfiles("test")
@SpringBootTest
class MemberServiceImplTest {
    @Autowired
    MemberServiceImpl memberServiceImpl;

    @Test
    @DisplayName("기수 반환 테스트")
    void getActualGeneration() {
        //given
        String email = "test";
        String domain = "@pos.idserve.net";

        //when
        AcademyGeneration generation_1 = memberServiceImpl.getGeneration(email + 22 + domain);
        AcademyGeneration generation_2 = memberServiceImpl.getGeneration(email + 23 + domain);
        AcademyGeneration generation_3 = memberServiceImpl.getGeneration(email + 24 + domain);
        AcademyGeneration generation_4 = memberServiceImpl.getGeneration(email + 25 + domain);
        AcademyGeneration generation_5 = memberServiceImpl.getGeneration(email + 26 + domain);
        AcademyGeneration generation_6 = memberServiceImpl.getGeneration(email + 27 + domain);
        AcademyGeneration generation_unknown = memberServiceImpl.getGeneration(email + domain);
        AcademyGeneration generation_invalid = memberServiceImpl.getGeneration(email + 77 + domain);

        //then
        assertEquals(generation_1, AcademyGeneration.GENERATION_1);
        assertEquals(generation_2, AcademyGeneration.GENERATION_2);
        assertEquals(generation_3, AcademyGeneration.GENERATION_3);
        assertEquals(generation_4, AcademyGeneration.GENERATION_4);
        assertEquals(generation_5, AcademyGeneration.GENERATION_5);
        assertEquals(generation_6, AcademyGeneration.GENERATION_6);
        assertEquals(generation_unknown, AcademyGeneration.UNKNOWN);
        assertEquals(generation_invalid, AcademyGeneration.UNKNOWN);
    }
}