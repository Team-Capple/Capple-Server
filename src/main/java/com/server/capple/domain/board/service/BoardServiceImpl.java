package com.server.capple.domain.board.service;

import com.server.capple.domain.board.dto.BoardResponse;
import com.server.capple.domain.board.entity.Board;
import com.server.capple.domain.board.entity.BoardType;
import com.server.capple.domain.board.mapper.BoardMapper;
import com.server.capple.domain.board.repository.BoardRepository;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.global.exception.RestApiException;
import com.server.capple.global.exception.errorCode.BoardErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
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
                .map(boardMapper::toBoardsGetByBoardTypeBoardInfo)
                .toList()
        );
    }

    @Override
    public BoardResponse.BoardDelete deleteBoard(Member member, Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RestApiException(BoardErrorCode.BOARD_NOT_FOUND));

        if (board.getWriter().getId() != member.getId()) {
            throw new RestApiException(BoardErrorCode.BOARD_NO_AUTHORIZATION);
        }

        board.delete();
        return boardMapper.toBoardDelete(board);
    }
}
