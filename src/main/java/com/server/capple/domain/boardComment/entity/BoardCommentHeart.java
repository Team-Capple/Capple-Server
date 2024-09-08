package com.server.capple.domain.boardComment.entity;

import com.server.capple.domain.member.entity.Member;
import com.server.capple.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BoardCommentHeart extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private BoardComment boardComment;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private boolean isLiked;

    public boolean toggleHeart() {
        this.isLiked = !this.isLiked;
        return isLiked;
    }
}
