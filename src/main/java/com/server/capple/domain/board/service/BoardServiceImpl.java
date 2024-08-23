package com.server.capple.domain.board.service;

import com.server.capple.domain.board.dto.BoardResponse;
import com.server.capple.domain.board.entity.Board;
import com.server.capple.domain.board.entity.BoardType;
import com.server.capple.domain.board.mapper.BoardMapper;
import com.server.capple.domain.board.repository.BoardHeartRedisRepository;
import com.server.capple.domain.board.repository.BoardRepository;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.global.exception.RestApiException;
import com.server.capple.global.exception.errorCode.BoardErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final BoardHeartRedisRepository boardHeartRedisRepository;
    private final BoardMapper boardMapper;

    @Override
    public BoardResponse.BoardCreate createBoard(Member member, BoardType boardType, String content) {
        Board board;
        if (content != null) {
            board = boardRepository.save(boardMapper.toBoard(member, boardType, content, 0, 0));
        } else {
            throw new RestApiException(BoardErrorCode.BOARD_BAD_REQUEST);
        }
        return boardMapper.toBoardCreate(board);
    }

    @Override
    public BoardResponse.BoardsGetByBoardType getBoardsByBoardType(BoardType boardType) {
        List<Board> boards = new ArrayList<>();
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
                .map(board -> boardMapper.toBoardsGetByBoardTypeBoardInfo(board, boardHeartRedisRepository.getBoardHeartsCount(board.getId())))
                .toList()
        );
    }

    @Override
    public BoardResponse.BoardDelete deleteBoard(Member member, Long boardId) {
        Board board = findBoard(boardId);
        if (board.getWriter().getId() != member.getId()) {
            throw new RestApiException(BoardErrorCode.BOARD_NO_AUTHORIZATION);
        }

        board.delete();
        return boardMapper.toBoardDelete(board);
    }

    @Override
    public BoardResponse.BoardsSearchByKeyword searchBoardsByKeyword(String keyword) {
        List<Board> boards = boardRepository.findBoardsByKeyword(keyword);
        return boardMapper.toBoardsSearchByKeyword(boards.stream()
                .map(board -> boardMapper.toBoardsSearchByKeywordBoardInfo(board, boardHeartRedisRepository.getBoardHeartsCount(board.getId())))
                .toList());
    }

    @Override
    public BoardResponse.BoardToggleHeart toggleBoardHeart(Member member, Long boardId) {
        Board board = findBoard(boardId);
        System.out.println(boardHeartRedisRepository.getBoardHeartCreateAt(board.getId(), member.getId()));

        Boolean isLiked = boardHeartRedisRepository.toggleBoardHeart(member.getId(), board.getId());
        return new BoardResponse.BoardToggleHeart(boardId, isLiked);
    }

    @Override
    public Board findBoard(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new RestApiException(BoardErrorCode.BOARD_NOT_FOUND));
    }


}
