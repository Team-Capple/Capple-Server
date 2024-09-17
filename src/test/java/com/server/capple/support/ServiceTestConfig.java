package com.server.capple.support;

import com.server.capple.domain.answer.dto.AnswerRequest;
import com.server.capple.domain.answer.entity.Answer;
import com.server.capple.domain.answer.repository.AnswerRepository;
import com.server.capple.domain.board.entity.Board;
import com.server.capple.domain.board.entity.BoardType;
import com.server.capple.domain.board.repository.BoardRepository;
import com.server.capple.domain.boardComment.dto.BoardCommentRequest;
import com.server.capple.domain.boardComment.entity.BoardComment;
import com.server.capple.domain.boardComment.repository.BoardCommentRepository;
import com.server.capple.domain.answerComment.dto.AnswerCommentRequest;
import com.server.capple.domain.answerComment.entity.AnswerComment;
import com.server.capple.domain.answerComment.repository.AnswerCommentRepository;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.member.entity.Role;
import com.server.capple.domain.member.repository.MemberRepository;
import com.server.capple.domain.question.entity.Question;
import com.server.capple.domain.question.entity.QuestionStatus;
import com.server.capple.domain.question.repository.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static java.lang.Boolean.FALSE;

@SpringBootTest
@ActiveProfiles("test")
public abstract class ServiceTestConfig {
    @Autowired
    protected MemberRepository memberRepository;
    @Autowired
    protected QuestionRepository questionRepository;
    @Autowired
    protected AnswerRepository answerRepository;
    @Autowired
    protected AnswerCommentRepository answerCommentRepository;
    @Autowired
    protected BoardRepository boardRepository;
    @Autowired
    BoardCommentRepository boardCommentRepository;
    protected Member member;
    protected Question liveQuestion;
    protected Question pendingQuestion;
    protected Question oldQuestion;
    protected Answer answer;
    protected Board board;
    protected BoardComment boardComment;
    protected AnswerComment answerComment;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @BeforeEach
    public void setUp() {
        member = createMember();
        liveQuestion = createLiveQuestion();
        pendingQuestion = createPendingQuestion();
        oldQuestion = createOldQuestion();
        answer = createAnswer();
        board = createBoard();
        boardComment = createBoardComment();
        answerComment = createAnswerComment();
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    protected Member createMember() {
        return memberRepository.save(
                Member.builder()
                        .nickname("루시")
                        .email("ksm@naver.com")
                        .profileImage("https://owori.s3.ap-northeast-2.amazonaws.com/story/capple_default_image_10635d7a-5f8c-4af2-b062-9a9420634eb3.png")
                        .role(Role.ROLE_ACADEMIER)
                        .email("tnals2384@gmail.com")
                        .sub("2384973284")
                        .build()
        );
    }

    protected Question createLiveQuestion() {
        return questionRepository.save(
                Question.builder()
                        .content("아카데미 러너 중 가장 마음에 드는 유형이 있나요?")
                        .questionStatus(QuestionStatus.LIVE)
                        .livedAt(LocalDateTime.now())
                        .build()
        );
    }

    protected Question createOldQuestion() {
        return questionRepository.save(
                Question.builder()
                        .content("오늘 뭐 먹을 거에요?")
                        .questionStatus(QuestionStatus.OLD)
                        .livedAt(LocalDateTime.of(2024, 04, 01, 00, 00, 00))
                        .build()
        );
    }

    protected Question createPendingQuestion() {
        return questionRepository.save(
                Question.builder()
                        .content("가장 좋아하는 음식은 무엇인가요?")
                        .questionStatus(QuestionStatus.PENDING)
                        .build()
        );
    }

    protected Answer createAnswer() {
        return answerRepository.save(Answer.builder()
                .content("나는 무자비한 사람이 좋아")
                .question(liveQuestion)
                .member(member)
                .build()
        );
    }

    protected AnswerRequest getAnswerRequest() {
        return AnswerRequest.builder()
                .answer("나는 와플을 좋아하는 사람이 좋아")
                .build();
    }

    protected Board createBoard() {
        return boardRepository.save(Board.builder()
                        .id(1L)
                        .boardType(BoardType.FREEBOARD)
                        .writer(member)
                        .content("오늘 밥먹을 사람!")
                        .commentCount(0)
                        .heartCount(0)
                        .isReport(FALSE)
                        .build());
    }

    protected BoardComment createBoardComment() {
        return boardCommentRepository.save(
                BoardComment.builder()
                        .writer(member)
                        .board(board)
                        .content("게시글 댓글")
                        .isReport(FALSE)
                        .heartCount(0)
                        .build());
    }

    protected BoardCommentRequest getBoardCommentRequest() {
        return new BoardCommentRequest("게시글 댓글");
    }

    protected AnswerCommentRequest getAnswerCommentRequest() {
        return AnswerCommentRequest.builder()
                .answerComment("댓글이 잘 달렸으면 좋겠어 . .")
                .build();
    }

    protected AnswerComment createAnswerComment() {
        return answerCommentRepository.save(AnswerComment.builder()
                .member(member)
                .answer(answer)
                .content("답변에 대한 댓글이어유")
                .build()
        );
    }
}
