package com.server.capple.domain.boardComment.mapper;

import com.server.capple.domain.board.dto.BoardResponse;
import com.server.capple.domain.board.entity.Board;
import com.server.capple.domain.boardComment.dao.BoardCommentInfoInterface;
import com.server.capple.domain.boardComment.dto.BoardCommentResponse;
import com.server.capple.domain.boardComment.dto.BoardCommentResponse.BoardCommentInfo;
import com.server.capple.domain.boardComment.entity.BoardComment;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.global.common.SliceResponse;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

@Component
public class BoardCommentMapper {
    public BoardComment toBoardComment(Member member, Board board, String comment) {
        return BoardComment.builder()
                .writer(member)
                .board(board)
                .content(comment)
                .isReport(Boolean.FALSE)
                .build();
    }

    //rdb
    public BoardCommentInfo toBoardCommentInfo(BoardComment comment, Boolean isLiked, Boolean isMine) {
        return BoardCommentInfo.builder()
                .boardCommentId(comment.getId())
                .writerId(comment.getWriter().getId())
                .content(comment.getContent())
                .heartCount(comment.getHeartCount())
                .isLiked(isLiked)
                .isMine(isMine)
                .isReport(comment.getIsReport())
                .createdAt(comment.getCreatedAt())
                .build();
    }

    public SliceResponse<BoardCommentInfo> toSliceBoardCommentInfo(Slice<BoardCommentInfoInterface> boardCommentSlice) {
        return SliceResponse.<BoardCommentInfo>builder()
                .number(boardCommentSlice.getNumber())
                .size(boardCommentSlice.getSize())
                .content(boardCommentSlice.getContent().stream().map(boardCommentInfoInterface ->
                                toBoardCommentInfo(boardCommentInfoInterface.getBoardComment(),
                                        boardCommentInfoInterface.getIsLike(),
                                        boardCommentInfoInterface.getIsMine()))
                        .toList()
                )
                .numberOfElements(boardCommentSlice.getNumberOfElements())
                .hasPrevious(boardCommentSlice.hasPrevious())
                .hasNext(boardCommentSlice.hasNext())
                .build();
    }
}
