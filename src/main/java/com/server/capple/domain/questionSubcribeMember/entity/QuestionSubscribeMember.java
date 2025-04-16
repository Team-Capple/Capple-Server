package com.server.capple.domain.questionSubcribeMember.entity;

import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.question.entity.Question;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionSubscribeMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;
}
