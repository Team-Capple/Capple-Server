package com.server.capple.domain.boardComment.service;

import com.server.capple.domain.board.entity.Board;
import com.server.capple.domain.board.service.BoardService;
import com.server.capple.domain.boardComment.dao.BoardCommentInfoInterface;
import com.server.capple.domain.boardComment.dto.BoardCommentRequest;
import com.server.capple.domain.boardComment.dto.BoardCommentResponse.BoardCommentId;
import com.server.capple.domain.boardComment.dto.BoardCommentResponse.BoardCommentInfo;
import com.server.capple.domain.boardComment.dto.BoardCommentResponse.ToggleBoardCommentHeart;
import com.server.capple.domain.boardComment.entity.BoardComment;
import com.server.capple.domain.boardComment.entity.BoardCommentHeart;
import com.server.capple.domain.boardComment.mapper.BoardCommentHeartMapper;
import com.server.capple.domain.boardComment.mapper.BoardCommentMapper;
import com.server.capple.domain.boardComment.repository.BoardCommentHeartRedisRepository;
import com.server.capple.domain.boardComment.repository.BoardCommentHeartRepository;
import com.server.capple.domain.boardComment.repository.BoardCommentRepository;
import com.server.capple.domain.boardSubscribeMember.service.BoardSubscribeMemberService;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.notifiaction.service.NotificationService;
import com.server.capple.global.common.SliceResponse;
import com.server.capple.global.exception.RestApiException;
import com.server.capple.global.exception.errorCode.CommentErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardCommentServiceImpl implements BoardCommentService {
    private final BoardService boardService;
    private final BoardCommentRepository boardCommentRepository;
    private final BoardCommentHeartRedisRepository boardCommentHeartRedisRepository;
    private final BoardCommentHeartRepository boardCommentHeartRepository;
    private final BoardCommentMapper boardCommentMapper;
    private final BoardCommentHeartMapper boardCommentHeartMapper;
    private final NotificationService notificationService;
    private final BoardSubscribeMemberService boardSubscribeMemberService;

    @Override
    @Transactional
    public BoardCommentId createBoardComment(Member member, Long boardId, BoardCommentRequest request) {
        Board board = boardService.findBoard(boardId);

        BoardComment boardComment = boardCommentRepository.save(
                boardCommentMapper.toBoardComment(member, board, request.getComment()));
        notificationService.sendBoardCommentNotification(member.getId(), board, boardComment); // 게시글 댓글 알림
        boardSubscribeMemberService.createBoardSubscribeMember(member, board); // 알림 리스트 추가

        board.increaseCommentCount();
        return new BoardCommentId(boardComment.getId());
    }

    @Override
    @Transactional
    public BoardCommentId updateBoardComment(Member member, Long commentId, BoardCommentRequest request) {
        BoardComment boardComment = findBoardComment(commentId);
        checkPermission(member, boardComment);

        boardComment.update(request.getComment());
        return new BoardCommentId(commentId);
    }

    @Override
    @Transactional
    public BoardCommentId deleteBoardComment(Member member, Long commentId) {
        BoardComment boardComment = findBoardComment(commentId);
        checkPermission(member, boardComment);

        Board board = boardComment.getBoard();

        boardComment.delete();
        board.decreaseCommentCount();

        return new BoardCommentId(boardComment.getId());
    }

    @Override
    @Transactional
    public ToggleBoardCommentHeart toggleBoardCommentHeart(Member member, Long boardCommentId) {
        BoardComment boardComment = findBoardComment(boardCommentId);
        //boardCommentHeart에 없다면 새로 저장
        BoardCommentHeart boardCommentHeart = boardCommentHeartRepository.findByMemberAndBoardComment(member, boardComment)
                .orElseGet(() -> {
                    BoardCommentHeart newHeart = boardCommentHeartMapper.toBoardCommentHeart(boardComment, member);
                    return boardCommentHeartRepository.save(newHeart);
                });
        boolean isLiked = boardCommentHeart.toggleHeart();
        boardComment.setHeartCount(boardCommentHeart.isLiked());
        if (isLiked && !boardComment.getWriter().getId().equals(member.getId())) {
            notificationService.sendBoardCommentHeartNotification(member.getId(), boardComment.getBoard(), boardComment);
        }
        return new ToggleBoardCommentHeart(boardCommentId, isLiked);
    }


    @Override
    public SliceResponse<BoardCommentInfo> getBoardCommentInfos(Member member, Long boardId, LocalDateTime thresholdDate, Pageable pageable) {
        thresholdDate = thresholdDate == null ? LocalDateTime.now() : thresholdDate;
        Slice<BoardCommentInfoInterface> sliceBoardCommentInfos = boardCommentRepository.findBoardCommentInfosByMemberAndBoardIdAndCreatedAtBefore(member, boardId, thresholdDate, pageable);
        return SliceResponse.toSliceResponse(sliceBoardCommentInfos, sliceBoardCommentInfos.getContent().stream().map(sliceBoardCommentInfo ->
                        boardCommentMapper.toBoardCommentInfo(
                                sliceBoardCommentInfo.getBoardComment(),
                                sliceBoardCommentInfo.getIsLike(),
                                sliceBoardCommentInfo.getIsMine()))
                .toList()
        );
    }

    private void checkPermission(Member member, BoardComment boardComment) {
        if (!member.getId().equals(boardComment.getWriter().getId()))
            throw new RestApiException(CommentErrorCode.COMMENT_NOT_FOUND);
    }

    @Override
    public BoardComment findBoardComment(Long commentId) {
        return boardCommentRepository.findById(commentId).orElseThrow(
                () -> new RestApiException(CommentErrorCode.COMMENT_NOT_FOUND));
    }
}
