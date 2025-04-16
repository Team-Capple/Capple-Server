package com.server.capple.domain.notifiaction.entity;

import com.server.capple.domain.member.entity.Member;
import com.server.capple.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Notification extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private NotificationLog notificationLog;
    @Enumerated(EnumType.STRING)
    private NotificationType type;
}
