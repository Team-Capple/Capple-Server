package com.server.capple.support;

import com.server.capple.domain.answer.repository.AnswerRepository;
import com.server.capple.domain.answer.service.AnswerConcurrentService;
import com.server.capple.domain.answer.service.AnswerService;
import com.server.capple.domain.answerComment.repository.AnswerCommentHeartRepository;
import com.server.capple.domain.answerComment.repository.AnswerCommentRepository;
import com.server.capple.domain.answerComment.service.AnswerCommentConcurrentService;
import com.server.capple.domain.answerComment.service.AnswerCommentService;
import com.server.capple.domain.board.repository.BoardRepository;
import com.server.capple.domain.board.service.BoardConcurrentService;
import com.server.capple.domain.board.service.BoardService;
import com.server.capple.domain.boardComment.repository.BoardCommentHeartRepository;
import com.server.capple.domain.boardComment.repository.BoardCommentRepository;
import com.server.capple.domain.boardComment.service.BoardCommentConcurrentService;
import com.server.capple.domain.boardComment.service.BoardCommentService;
import com.server.capple.domain.member.repository.MemberRepository;
import com.server.capple.domain.notifiaction.service.NotificationService;
import com.server.capple.domain.question.repository.QuestionRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public abstract class ConcurrentTestsConfig {
    @Autowired
    protected MemberRepository memberRepository;
    @Autowired
    protected BoardRepository boardRepository;
    @Autowired
    protected BoardCommentRepository boardCommentRepository;
    @Autowired
    protected BoardCommentHeartRepository boardCommentHeartRepository;
    @Autowired
    protected BoardCommentConcurrentService boardCommentConcurrentService;
    @Autowired
    protected BoardCommentService boardCommentService;
    @Autowired
    protected EntityManager entityManager;
    @Autowired
    protected BoardConcurrentService boardConcurrentService;
    @Autowired
    protected BoardService boardService;
    @Autowired
    protected JdbcTemplate jdbcTemplate;
    @Autowired
    protected QuestionRepository questionRepository;
    @Autowired
    protected AnswerRepository answerRepository;
    @Autowired
    protected AnswerCommentRepository answerCommentRepository;
    @Autowired
    protected AnswerCommentHeartRepository answerCommentHeartRepository;
    @Autowired
    protected AnswerCommentConcurrentService answerCommentConcurrentService;
    @Autowired
    protected AnswerCommentService answerCommentService;
    @Autowired
    protected RedisTemplate redisTemplate;
    @Autowired
    protected AnswerConcurrentService answerConcurrentService;
    @Autowired
    protected AnswerService answerService;
    @MockBean
    protected NotificationService notificationService;
}
