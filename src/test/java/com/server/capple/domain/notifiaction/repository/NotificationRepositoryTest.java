package com.server.capple.domain.notifiaction.repository;

import com.server.capple.domain.board.entity.Board;
import com.server.capple.domain.board.entity.BoardType;
import com.server.capple.domain.board.repository.BoardRepository;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.member.repository.MemberRepository;
import com.server.capple.domain.notifiaction.dto.NotificationDBResponse.NotificationDBInfo;
import com.server.capple.domain.notifiaction.entity.Notification;
import com.server.capple.domain.notifiaction.entity.NotificationLog;
import com.server.capple.domain.notifiaction.entity.NotificationType;
import com.server.capple.domain.notifiaction.mapper.NotificationMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.server.capple.domain.member.entity.Role.ROLE_ACADEMIER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@ActiveProfiles("test")
@SpringBootTest
@DisplayName("notification 리포지토리로 ")
class NotificationRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private NotificationRepository notificationRepository;

    @AfterEach
    void tearDown() {
        notificationRepository.deleteAll();
        boardRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("자신의 게시글을 제외한 다른 사람이 작성한 게시글 알림을 조회할 수 있다")
    void findByMemberId2() {
        // given
        Member member1 = Member.builder()
            .nickname("member1")
            .email("member1")
            .sub("member1")
            .role(ROLE_ACADEMIER)
            .build();
        Member member2 = Member.builder()
            .nickname("member2")
            .email("member2")
            .sub("member2")
            .role(ROLE_ACADEMIER)
            .build();
        Member member3 = Member.builder()
            .nickname("member3")
            .email("member3")
            .sub("member3")
            .role(ROLE_ACADEMIER)
            .build();
        Member savedMember1 = memberRepository.save(member1);
        Member savedMember2 = memberRepository.save(member2);
        Member savedMember3 = memberRepository.save(member3);
        Board board2 = Board.builder()
            .boardType(BoardType.FREEBOARD)
            .content("boardContent2")
            .writer(savedMember2)
            .isReport(false)
            .build();
        Board board3 = Board.builder()
            .boardType(BoardType.FREEBOARD)
            .content("boardContent3")
            .writer(savedMember3)
            .isReport(false)
            .build();
        Board savedBoard2 = boardRepository.save(board2);
        Board savedBoard3 = boardRepository.save(board3);
        NotificationLog notificationLog2 = new NotificationMapper()
            .toNotificationLog(savedBoard2);
        NotificationLog notificationLog3 = new NotificationMapper()
            .toNotificationLog(savedBoard3);
        Notification notification2 = new NotificationMapper()
            .toNotification(savedMember2, notificationLog2, NotificationType.NEW_FREE_BOARD);
        Notification notification3 = new NotificationMapper()
            .toNotification(savedMember3, notificationLog3, NotificationType.NEW_FREE_BOARD);
        notificationRepository.saveAll(List.of(notification2, notification3));

        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));

        // when
        Slice<NotificationDBInfo> result1 = notificationRepository.findByMemberId(savedMember1, -1L, pageRequest);
        Slice<NotificationDBInfo> result2 = notificationRepository.findByMemberId(savedMember2, -1L, pageRequest);
        Slice<NotificationDBInfo> result3 = notificationRepository.findByMemberId(savedMember3, -1L, pageRequest);

        // then
        assertThat(result1.getContent()).hasSize(2)
            .extracting("notification.notificationLog.board.content")
            .containsExactlyInAnyOrder(
                tuple(board2.getContent()),
                tuple(board3.getContent())
            )
        ;
        assertThat(result2.getContent()).hasSize(1)
            .extracting("notification.notificationLog.board.content")
            .containsExactlyInAnyOrder(
                tuple(board3.getContent())
            )
        ;
        assertThat(result3.getContent()).hasSize(1)
            .extracting("notification.notificationLog.board.content")
            .containsExactlyInAnyOrder(
                tuple(board2.getContent())
            )
        ;
    }
}