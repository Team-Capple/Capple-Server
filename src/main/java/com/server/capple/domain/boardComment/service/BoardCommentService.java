package com.server.capple.domain.boardComment.service;

import com.server.capple.domain.boardComment.dto.BoardCommentRequest;
import com.server.capple.domain.boardComment.dto.BoardCommentResponse.BoardCommentId;
import com.server.capple.domain.boardComment.dto.BoardCommentResponse.BoardCommentInfo;
import com.server.capple.domain.boardComment.dto.BoardCommentResponse.ToggleBoardCommentHeart;
import com.server.capple.domain.boardComment.entity.BoardComment;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.global.common.SliceResponse;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface BoardCommentService {
    BoardCommentId createBoardComment(Member member, Long boardId, BoardCommentRequest request);
    BoardCommentId updateBoardComment(Member member, Long commentId,BoardCommentRequest request);
    BoardCommentId deleteBoardComment(Member member, Long commentId);
    ToggleBoardCommentHeart toggleBoardCommentHeart(Member member, Long commentId);
    SliceResponse<BoardCommentInfo> getBoardCommentInfos(Member member, Long boardId, LocalDateTime thresholdDate, Pageable pageable);
    BoardComment findBoardComment(Long commentId);
}
