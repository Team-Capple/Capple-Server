package com.server.capple.domain.boardComment.service;

import com.server.capple.domain.board.entity.Board;
import com.server.capple.domain.board.service.BoardService;
import com.server.capple.domain.boardComment.dto.BoardCommentRequest;
import com.server.capple.domain.boardComment.dto.BoardCommentResponse.BoardCommentId;
import com.server.capple.domain.boardComment.dto.BoardCommentResponse.BoardCommentInfo;
import com.server.capple.domain.boardComment.dto.BoardCommentResponse.BoardCommentInfos;
import com.server.capple.domain.boardComment.dto.BoardCommentResponse.ToggleBoardCommentHeart;
import com.server.capple.domain.boardComment.entity.BoardComment;
import com.server.capple.domain.boardComment.entity.BoardCommentHeart;
import com.server.capple.domain.boardComment.mapper.BoardCommentHeartMapper;
import com.server.capple.domain.boardComment.mapper.BoardCommentMapper;
import com.server.capple.domain.boardComment.repository.BoardCommentHeartRedisRepository;
import com.server.capple.domain.boardComment.repository.BoardCommentHeartRepository;
import com.server.capple.domain.boardComment.repository.BoardCommentRepository;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.member.service.MemberService;
import com.server.capple.global.exception.RestApiException;
import com.server.capple.global.exception.errorCode.CommentErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardCommentServiceImpl implements BoardCommentService {
    private final MemberService memberService;
    private final BoardService boardService;
    private final BoardCommentRepository boardCommentRepository;
    private final BoardCommentHeartRedisRepository boardCommentHeartRedisRepository;
    private final BoardCommentHeartRepository boardCommentHeartRepository;
    private final BoardCommentMapper boardCommentMapper;
    private final BoardCommentHeartMapper boardCommentHeartMapper;

    @Override
    @Transactional
    public BoardCommentId createBoardComment(Member member, Long boardId, BoardCommentRequest request) {
        Member loginMember = memberService.findMember(member.getId());
        Board board = boardService.findBoard(boardId);

        BoardComment boardComment = boardCommentRepository.save(
                boardCommentMapper.toBoardComment(loginMember, board, request.getComment()));

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
        return new ToggleBoardCommentHeart(boardCommentId, isLiked);
    }

    //rdb
    @Override
    public BoardCommentInfos getBoardCommentInfos(Member member, Long boardId) {
        List<BoardCommentInfo> commentInfos = boardCommentRepository
                .findBoardCommentByBoardIdOrderByCreatedAt(boardId).stream().map(
                        comment -> {
                            Boolean isLiked = boardCommentHeartRepository.findByMemberAndBoardComment(member, comment)
                                    .isPresent();
                            Boolean isMine = comment.getMember().getId().equals(member.getId());
                            return boardCommentMapper.toBoardCommentInfo(comment, isLiked, isMine);
                        }).toList();

        return new BoardCommentInfos(commentInfos);
    }

    private void checkPermission(Member member, BoardComment boardComment) {
        Member loginMember = memberService.findMember(member.getId());

        if (!loginMember.getId().equals(boardComment.getMember().getId()))
            throw new RestApiException(CommentErrorCode.COMMENT_NOT_FOUND);
    }

    @Override
    public BoardComment findBoardComment(Long commentId) {
        return boardCommentRepository.findById(commentId).orElseThrow(
                () -> new RestApiException(CommentErrorCode.COMMENT_NOT_FOUND));
    }
}
