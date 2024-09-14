package com.server.capple.domain.question.entity;

import com.server.capple.domain.answer.entity.Answer;
import com.server.capple.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
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
    private Long id;

    @Column(nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuestionStatus questionStatus;

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    private List<Answer> answers = new ArrayList<>();

    private LocalDateTime livedAt;

//    @Column(nullable = false)
//    private Integer commentCount;

    //question Status를 바꾸는 함수
    public void setQuestionStatus(QuestionStatus questionStatus) {
        this.questionStatus = questionStatus;

        if (questionStatus.equals(QuestionStatus.LIVE))
            this.livedAt = LocalDateTime.now();
    }

//    public void increaseCommentCount() {
//        this.commentCount += 1;
//    }
//
//    public void decreaseCommentCount() {
//        this.commentCount -= 1;
//    }

}
