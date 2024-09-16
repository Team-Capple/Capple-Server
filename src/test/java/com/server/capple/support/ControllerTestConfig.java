package com.server.capple.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.capple.config.security.auth.CustomUserDetails;
import com.server.capple.config.security.auth.service.JpaUserDetailService;
import com.server.capple.config.security.jwt.service.JwtService;
import com.server.capple.domain.answer.dto.AnswerRequest;
import com.server.capple.domain.answer.dto.AnswerResponse;
import com.server.capple.domain.answer.dto.AnswerResponse.MemberAnswerList;
import com.server.capple.domain.answer.entity.Answer;
import com.server.capple.domain.answerComment.dto.AnswerCommentRequest;
import com.server.capple.domain.answerComment.dto.AnswerCommentResponse.AnswerCommentInfo;
import com.server.capple.domain.answerComment.dto.AnswerCommentResponse.AnswerCommentInfos;
import com.server.capple.domain.boardComment.dto.BoardCommentRequest;
import com.server.capple.domain.boardComment.dto.BoardCommentResponse.BoardCommentInfo;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.member.entity.Role;
import com.server.capple.domain.question.entity.Question;
import com.server.capple.domain.question.entity.QuestionStatus;
import com.server.capple.global.common.SliceResponse;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public abstract class ControllerTestConfig {
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    protected Member member;
    protected String jwt;
    @Autowired
    JwtService jwtService;
    @MockBean
    JpaUserDetailService jpaUserDetailService;
    protected Question question;
    protected Answer answer;

    @BeforeEach
    public void setUp() {
        member = createMember();
        question = createQuestion();
        answer = createAnswer();
        jwt = createJwt(member);
        when(jpaUserDetailService.loadUserByUsername(member.getId().toString())).thenReturn(new CustomUserDetails(member));
    }

    protected Member createMember() {
        return Member.builder()
                .id(1L)
                .role(Role.ROLE_ACADEMIER)
                .sub("2384973284")
                .email("tnals2384@gmail.com")
                .profileImage("https://owori.s3.ap-northeast-2.amazonaws.com/story/capple_default_image_10635d7a-5f8c-4af2-b062-9a9420634eb3.png")
                .email("ksm@naver.com")
                .nickname("루시")
                .build();
    }

    protected String createJwt(Member member) {
        return jwtService.createJwt(member.getId(), member.getRole().getName(), "access");
    }

    protected Question createQuestion() {
        return Question.builder()
                .id(1L)
                .content("아카데미 러너 중 가장 마음에 드는 유형이 있나요?")
                .questionStatus(QuestionStatus.LIVE)
                .build();
    }

    protected Answer createAnswer() {
        return Answer.builder()
                .id(1L)
                .content("나는 무자비한 사람이 좋아")
                .question(question)
                .member(member)
                .build();
    }

    protected AnswerRequest getAnswerRequest() {
        return AnswerRequest.builder()
                .answer("나는 와플을 좋아하는 사람이 좋아")
                .build();
    }

    protected MemberAnswerList getMemberAnswerList () {
        List<AnswerResponse.MemberAnswerInfo> memberAnswerInfos = List.of(AnswerResponse.MemberAnswerInfo.builder()
                .questionId(answer.getQuestion().getId())
                .answerId(answer.getId())
                .writerId(member.getId())
                .profileImage(answer.getMember().getProfileImage())
                .content(answer.getContent())
                .heartCount(1)
                .build());

        return new MemberAnswerList(memberAnswerInfos);
    }

    protected BoardCommentRequest getBoardCommentRequest() {
        return new BoardCommentRequest("게시글 댓글");
    }

    protected SliceResponse<BoardCommentInfo> getSliceBoardCommentInfos() {
        List<BoardCommentInfo> commentInfos =
                List.of(BoardCommentInfo.builder()
                        .boardCommentId(1L)
                        .writerId(member.getId())
                        .content("댓글")
                        .createdAt(LocalDateTime.now())
                        .heartCount(2)
                        .isLiked(TRUE)
                        .isReport(FALSE)
                        .build());


        return SliceResponse.<BoardCommentInfo>builder()
                .number(0)
                .size(10)
                .content(commentInfos)
                .numberOfElements(1)
                .hasPrevious(false)
                .hasNext(true)
                .build();
    }

    protected AnswerCommentRequest getAnswerCommentRequest() {
        return AnswerCommentRequest.builder()
                .answerComment("댓글이 잘 달렸으면 좋겠어 . .")
                .build();
    }

    protected AnswerCommentInfos getAnswerCommentInfos () {
        List<AnswerCommentInfo> answerCommentInfos = List.of(AnswerCommentInfo.builder()
                .answerCommentId(1L)
                .writerId(member.getId())
                .content("댓글 1")
                .createdAt(LocalDateTime.of(2022, 11, 1, 12, 02))
                .heartCount(3L)
                .build());

        return new AnswerCommentInfos(answerCommentInfos);
    }

}
