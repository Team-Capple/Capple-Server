package com.server.capple.domain.board.service;

import com.server.capple.domain.board.dao.BoardInfoInterface;
import com.server.capple.domain.board.dto.BoardResponse.BoardId;
import com.server.capple.domain.board.dto.BoardResponse.BoardsGetBoardInfos;
import com.server.capple.domain.board.dto.BoardResponse.ToggleBoardHeart;
import com.server.capple.domain.board.entity.Board;
import com.server.capple.domain.board.entity.BoardHeart;
import com.server.capple.domain.board.entity.BoardType;
import com.server.capple.domain.board.mapper.BoardHeartMapper;
import com.server.capple.domain.board.mapper.BoardMapper;
import com.server.capple.domain.board.repository.BoardHeartRedisRepository;
import com.server.capple.domain.board.repository.BoardHeartRepository;
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
@Transactional(readOnly = true)
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final BoardHeartRedisRepository boardHeartRedisRepository;
    private final BoardMapper boardMapper;
    private final BoardHeartRepository boardHeartRepository;
    private final BoardHeartMapper boardHeartMapper;
    private final NotificationService notificationService;
    private final BoardSubscribeMemberService boardSubscribeMemberService;

    @Override
    @Transactional
    public BoardId createBoard(Member member, BoardType boardType, String content) {
        Board board = boardRepository.save(boardMapper.toBoard(member, boardType, content));
        boardSubscribeMemberService.createBoardSubscribeMember(member, board);

        return new BoardId(board.getId());
    }

    @Override
    public BoardsGetBoardInfos getBoardsByBoardType(Member member, BoardType boardType) {
        List<BoardInfoInterface> boardInfos = boardRepository.findBoardInfosByMemberAndBoardType(member, boardType);

        return new BoardsGetBoardInfos(boardInfos.stream()
                .map(boardInfo -> boardMapper.toBoardsGetBoardInfo(boardInfo.getBoard(), boardInfo.getIsLike(), boardInfo.getIsMine()))
                .toList()
        );
    }

    /*
    redis 성능 테스트 용
     */
    @Override
    public BoardsGetBoardInfos getBoardsByBoardTypeWithRedis(Member member, BoardType boardType) {
        List<Board> boards;
        if (boardType == null) {
            boards = boardRepository.findAll();
        } else {
            boards = boardRepository.findBoardsByBoardType(boardType);
        }

        return new BoardsGetBoardInfos(boards.stream()
                .map(board -> {
                    int heartCount = boardHeartRedisRepository.getBoardHeartsCount(board.getId());
                    boolean isLiked = boardHeartRedisRepository.isMemberLikedBoard(member.getId(), board.getId());
                    boolean isMine = board.getWriter().getId().equals(member.getId());
                    return boardMapper.toBoardsGetBoardInfo(board, heartCount, isLiked, isMine);
                })
                .toList()
        );
    }

    @Override
    public BoardsGetBoardInfos searchBoardsByKeyword(Member member, String keyword) {
        List<BoardInfoInterface> boardInfos = boardRepository.findBoardInfosByMemberAndKeyword(member, keyword);

        return new BoardsGetBoardInfos(boardInfos.stream()
                .map(boardInfo -> boardMapper.toBoardsGetBoardInfo(boardInfo.getBoard(), boardInfo.getIsLike(), boardInfo.getIsMine()))
                .toList()
        );
    }

    @Override
    @Transactional
    public BoardId deleteBoard(Member member, Long boardId) {
        Board board = findBoard(boardId);
        checkPermission(member, board);

        board.delete();
        boardSubscribeMemberService.deleteBoardSubscribeMemberByBoardId(boardId);
        return new BoardId(board.getId());
    }

    @Override
    @Transactional
    public ToggleBoardHeart toggleBoardHeart(Member member, Long boardId) {
        Board board = findBoard(boardId);
        // 좋아요 눌렀는지 확인
        //boardHeart에 없다면 새로 저장
        BoardHeart boardHeart = boardHeartRepository.findByMemberAndBoard(member, board)
                .orElseGet(() -> {
                    BoardHeart newHeart = boardHeartMapper.toBoardHeart(board, member);
                    return boardHeartRepository.save(newHeart);
                });

        boolean isLiked = boardHeart.toggleHeart();
        board.setHeartCount(boardHeart.isLiked());

        if (isLiked) notificationService.sendBoardHeartNotification(member.getId(), board);
        return new ToggleBoardHeart(boardId, isLiked);
    }

    private void checkPermission(Member member, Board board) {
        if (!member.getId().equals(board.getWriter().getId()))
            throw new RestApiException(BoardErrorCode.BOARD_NO_AUTHORIZATION);
    }

    @Override
    public Board findBoard(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new RestApiException(BoardErrorCode.BOARD_NOT_FOUND));
    }
}
