package com.server.capple.domain.board.entity;

import com.server.capple.domain.member.entity.Member;
import com.server.capple.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SQLRestriction("deleted_at is null")
@DynamicInsert
public class Board extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member writer;

    @Column(nullable = false)
    private BoardType boardType;

    @Column(nullable = false)
    private String content;

    @ColumnDefault("0")
    private Integer commentCount;

    @ColumnDefault("0")
    private Integer heartCount;

    public void setHeartCount(boolean isLiked) {
        if (isLiked) {
            this.heartCount++;
        } else {
            this.heartCount--;
        }
    }

    public void increaseCommentCount() {
        this.commentCount++;
    }

    public void decreaseCommentCount() {
        this.commentCount--;
    }
}
