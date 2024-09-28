package com.server.capple.domain.board.service;

import com.server.capple.domain.board.dao.BoardInfoInterface;
import com.server.capple.domain.board.dto.BoardResponse.BoardId;
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
import com.server.capple.global.common.SliceResponse;
import com.server.capple.global.exception.RestApiException;
import com.server.capple.global.exception.errorCode.BoardErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import static com.server.capple.domain.board.dto.BoardResponse.BoardInfo;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;
    private final BoardMapper boardMapper;
    private final BoardHeartRepository boardHeartRepository;
    private final BoardHeartMapper boardHeartMapper;
    private final NotificationService notificationService;
    private final BoardHeartRedisRepository boardHeartRedisRepository;
    private final BoardSubscribeMemberService boardSubscribeMemberService;
    private final BoardCountService boardCountService;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    @Transactional
    public BoardId createBoard(Member member, BoardType boardType, String content) {
        Board board = boardRepository.save(boardMapper.toBoard(member, boardType, content));
        boardSubscribeMemberService.createBoardSubscribeMember(member, board);
        applicationEventPublisher.publishEvent(new BoardCreatedEvent());
        return new BoardId(board.getId());
    }

    @Override
    public SliceResponse<BoardInfo> getBoardsByBoardType(Member member, BoardType boardType, Pageable pageable) {
        Slice<BoardInfoInterface> sliceBoardInfos = boardRepository.findBoardInfosByMemberAndBoardType(member, boardType, pageable);

        return SliceResponse.toSliceResponse(sliceBoardInfos, sliceBoardInfos.getContent().stream().map(sliceBoardInfo ->
                    boardMapper.toBoardInfo(
                        sliceBoardInfo.getBoard(),
                        sliceBoardInfo.getIsLike(),
                        sliceBoardInfo.getIsMine()))
                .toList(),
            boardCountService.getBoardCount()
        );
    }

    @Override
    public SliceResponse<BoardInfo> searchBoardsByKeyword(Member member, String keyword, Pageable pageable) {
        Slice<BoardInfoInterface> sliceBoardInfos = boardRepository.findBoardInfosByMemberAndKeyword(member, keyword, pageable);

        return SliceResponse.toSliceResponse(sliceBoardInfos, sliceBoardInfos.getContent().stream().map(sliceBoardInfo ->
                boardMapper.toBoardInfo(
                    sliceBoardInfo.getBoard(),
                    sliceBoardInfo.getIsLike(),
                    sliceBoardInfo.getIsMine()))
            .toList(), null
        );
    }

    /*
    redis 성능 테스트 용
     */
    @Override
    public SliceResponse<BoardInfo> getBoardsByBoardTypeWithRedis(Member member, BoardType boardType, Pageable pageable) {
        Slice<BoardInfoInterface> sliceBoardInfos = boardRepository.findBoardInfosForRedis(member, boardType, pageable);

        return SliceResponse.toSliceResponse(sliceBoardInfos, sliceBoardInfos.getContent().stream().map(sliceBoardInfo -> {
                    int heartCount = boardHeartRedisRepository.getBoardHeartsCount(sliceBoardInfo.getBoard().getId());
                    boolean isLiked = boardHeartRedisRepository.isMemberLikedBoard(member.getId(), sliceBoardInfo.getBoard().getId());
                    return boardMapper.toBoardInfo(
                        sliceBoardInfo.getBoard(),
                        heartCount,
                        isLiked,
                        sliceBoardInfo.getIsMine());
                })
                .toList(),
            boardCountService.getBoardCount());
    }

    @Override
    @Transactional
    public BoardId deleteBoard(Member member, Long boardId) {
        Board board = findBoard(boardId);
        checkPermission(member, board);

        applicationEventPublisher.publishEvent(new BoardCreatedEvent());

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

    static class BoardCreatedEvent {
    }

    @TransactionalEventListener(BoardCreatedEvent.class)
    public void handleBoardCreatedEvent() {
        boardCountService.updateBoardCount();
    }
}
