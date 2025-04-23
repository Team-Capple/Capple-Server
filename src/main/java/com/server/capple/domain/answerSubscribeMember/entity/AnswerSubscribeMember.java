package com.server.capple.domain.answerSubscribeMember.entity;

import com.server.capple.domain.answer.entity.Answer;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AnswerSubscribeMember extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_subscribe_member_id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "answer_id", nullable = false)
    private Answer answer;
    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
}
