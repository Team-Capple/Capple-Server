package com.server.capple.domain.boardComment.service;

import com.server.capple.domain.boardComment.dto.BoardCommentRequest;
import com.server.capple.domain.boardComment.dto.BoardCommentResponse.BoardCommentHeart;
import com.server.capple.domain.boardComment.dto.BoardCommentResponse.BoardCommentId;
import com.server.capple.domain.boardComment.dto.BoardCommentResponse.BoardCommentInfos;
import com.server.capple.domain.boardComment.entity.BoardComment;
import com.server.capple.domain.member.entity.Member;

public interface BoardCommentService {
    BoardCommentId createBoardComment(Member member, Long boardId, BoardCommentRequest request);
    BoardCommentId updateBoardComment(Member member, Long commentId,BoardCommentRequest request);
    BoardCommentId deleteBoardComment(Member member, Long commentId);
    BoardCommentHeart heartBoardComment(Member member, Long commentId);
    BoardCommentInfos getBoardCommentInfos(Member member, Long boardId);
    BoardComment findBoardComment(Long commentId);
}
