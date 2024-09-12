package com.server.capple.domain.boardComment.mapper;

import com.server.capple.domain.boardComment.entity.BoardComment;
import com.server.capple.domain.boardComment.entity.BoardCommentHeart;
import com.server.capple.domain.member.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class BoardCommentHeartMapper {

    public BoardCommentHeart toBoardCommentHeart(BoardComment boardComment, Member member) {
        return BoardCommentHeart.builder()
                .boardComment(boardComment)
                .member(member)
                .isLiked(false)
                .build();
    }
}
