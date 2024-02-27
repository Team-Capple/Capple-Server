package com.server.capple.domain.question.entity;

import com.server.capple.domain.answer.entity.Answer;
import com.server.capple.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SQLRestriction("deleted_at is null")
@DynamicInsert
public class Question extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private QuestionStatus questionStatus;

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    private List<Answer> answers = new ArrayList<>();


}
