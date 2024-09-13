package com.server.capple.domain.boardComment.mapper;

import com.server.capple.domain.board.entity.Board;
import com.server.capple.domain.boardComment.dto.BoardCommentResponse.BoardCommentInfo;
import com.server.capple.domain.boardComment.entity.BoardComment;
import com.server.capple.domain.member.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class BoardCommentMapper {
    public BoardComment toBoardComment(Member member, Board board, String comment) {
        return BoardComment.builder()
                .member(member)
                .board(board)
                .content(comment)
                .heartCount(0)
                .build();
    }

    public BoardCommentInfo toBoardCommentInfo(BoardComment comment, Integer boardHeart, Boolean isLiked, Boolean isMine) {
        return BoardCommentInfo.builder()
                .boardCommentId(comment.getId())
                .writerId(comment.getMember().getId())
                .content(comment.getContent())
                .heartCount(boardHeart)
                .isLiked(isLiked)
                .isMine(isMine)
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
