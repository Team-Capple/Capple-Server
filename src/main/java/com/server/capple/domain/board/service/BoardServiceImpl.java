package com.server.capple.domain.board.service;

import com.server.capple.domain.board.dto.BoardResponse.*;
import com.server.capple.domain.board.entity.Board;
import com.server.capple.domain.board.entity.BoardType;
import com.server.capple.domain.board.mapper.BoardMapper;
import com.server.capple.domain.board.repository.BoardHeartRedisRepository;
import com.server.capple.domain.board.repository.BoardRepository;
import com.server.capple.domain.boardSubscribeMember.service.BoardSubscribeMemberService;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.notifiaction.service.NotificationService;
import com.server.capple.global.exception.RestApiException;
import com.server.capple.global.exception.errorCode.BoardErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final BoardHeartRedisRepository boardHeartRedisRepository;
    private final BoardMapper boardMapper;
    private final NotificationService notificationService;
    private final BoardSubscribeMemberService boardSubscribeMemberService;

    @Override
    public BoardCreate createBoard(Member member, BoardType boardType, String content) {
        Board board;
        if (content != null) {
            board = boardRepository.save(boardMapper.toBoard(member, boardType, content));
        } else {
            throw new RestApiException(BoardErrorCode.BOARD_BAD_REQUEST);
        }
        boardSubscribeMemberService.createBoardSubscribeMember(member, board);
        return boardMapper.toBoardCreate(board);
    }

    @Override
    public BoardsGetByBoardType getBoardsByBoardType(Member member, BoardType boardType) {
        List<Board> boards;
        if (boardType == null) {
            boards = boardRepository.findAll();
        } else if (boardType == BoardType.FREEBOARD) {
            boards = boardRepository.findBoardsByBoardType(BoardType.FREEBOARD);
        } else if (boardType == BoardType.HOTBOARD) {
            boards = boardRepository.findBoardsByBoardType(BoardType.HOTBOARD);
        } else {
            throw new RestApiException(BoardErrorCode.BOARD_BAD_REQUEST);
        }
        return boardMapper.toBoardsGetByBoardType(boards.stream()
                // TODO: BoardReport 관련 테이블 구현 후 수정 요망
                .map(board -> {
                    int heartCount = boardHeartRedisRepository.getBoardHeartsCount(board.getId());
                    boolean isLiked = boardHeartRedisRepository.isMemberLikedBoard(member.getId(), board.getId());
                    boolean isMine = board.getWriter().getId().equals(member.getId());
                    return boardMapper.toBoardsGetByBoardTypeBoardInfo(board, heartCount, isLiked, isMine, false);
                })
                .toList()
        );
    }

    @Override
    public BoardDelete deleteBoard(Member member, Long boardId) {
        Board board = findBoard(boardId);
        if (board.getWriter().getId() != member.getId()) {
            throw new RestApiException(BoardErrorCode.BOARD_NO_AUTHORIZATION);
        }

        board.delete();
        boardSubscribeMemberService.deleteBoardSubscribeMemberByBoardId(boardId);
        return boardMapper.toBoardDelete(board);
    }

    @Override
    public BoardsSearchByKeyword searchBoardsByKeyword(String keyword) {
        List<Board> boards = boardRepository.findBoardsByKeyword(keyword);
        return boardMapper.toBoardsSearchByKeyword(boards.stream()
                .map(board -> boardMapper.toBoardsSearchByKeywordBoardInfo(board, boardHeartRedisRepository.getBoardHeartsCount(board.getId())))
                .toList());
    }

    @Override
    @Transactional
    public ToggleBoardHeart toggleBoardHeart(Member member, Long boardId) {
        Board board = findBoard(boardId);

        Boolean isLiked = boardHeartRedisRepository.toggleBoardHeart(member.getId(), board.getId());
        if (isLiked) notificationService.sendBoardHeartNotification(member.getId(), board);

        return new ToggleBoardHeart(boardId, isLiked);
    }

    @Override
    public Board findBoard(Long boardId) {
        return boardRepository.findById(boardId)
            .orElseThrow(() -> new RestApiException(BoardErrorCode.BOARD_NOT_FOUND));
    }
}
