package com.server.capple.domain.answerComment.entity;

import com.server.capple.domain.answer.entity.Answer;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SQLRestriction("deleted_at is null")
@DynamicInsert
public class AnswerComment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "answer_id", nullable = false)
    private Answer answer;

    @Column(nullable = false)
    private String content;

    public void update(String content) {
        this.content = content;
    }
}
