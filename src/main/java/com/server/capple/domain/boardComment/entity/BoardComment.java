package com.server.capple.domain.boardComment.entity;

import com.server.capple.domain.board.entity.Board;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SQLRestriction("deleted_at is null")
@DynamicInsert
@DynamicUpdate
public class BoardComment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Boolean isReport;

    @ColumnDefault("0")
    private Integer heartCount;

    public void update(String content) {
        this.content = content;
    }

    public void submitReport() { this.isReport = Boolean.TRUE; }

    public void cancelReport() { this.isReport = Boolean.FALSE; }
    public void setHeartCount(boolean isLiked) {
        if (isLiked) {
            this.heartCount++;
        } else {
            this.heartCount--;
        }
    }
}
