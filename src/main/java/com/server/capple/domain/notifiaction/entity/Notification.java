package com.server.capple.domain.notifiaction.entity;

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
    @Column(nullable = false)
    private Long memberId;
    @Column(nullable = false)
    private String title;
    private String subtitle;
    @Column(nullable = false)
    private String content;
    @Column(nullable = false)
    private String boardId;
    private String boardCommentId;
}
