package com.server.capple.domain.boardComment.mapper;

import com.server.capple.domain.board.entity.Board;
import com.server.capple.domain.boardComment.dto.BoardCommentResponse.BoardCommentInfo;
import com.server.capple.domain.boardComment.entity.BoardComment;
import com.server.capple.domain.member.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class BoardCommentMapper {
    public BoardComment toBoardCommentEntity(Member member, Board board, String comment) {
        return BoardComment.builder()
                .member(member)
                .board(board)
                .content(comment)
                .build();
    }

    public BoardCommentInfo toBoardCommentInfo(BoardComment comment, Long heartCount, Boolean isLiked) {
        return BoardCommentInfo.builder()
                .boardCommentId(comment.getId())
                .writerId(comment.getMember().getId())
                .content(comment.getContent())
                .heartCount(heartCount)
                .isLiked(isLiked)
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
