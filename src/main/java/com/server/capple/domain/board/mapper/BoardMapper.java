package com.server.capple.domain.board.mapper;

import com.server.capple.domain.board.dao.BoardInfoInterface;
import com.server.capple.domain.board.dto.BoardResponse.BoardInfo;
import com.server.capple.domain.board.entity.Board;
import com.server.capple.domain.board.entity.BoardType;
import com.server.capple.domain.board.repository.BoardHeartRedisRepository;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.global.common.SliceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BoardMapper {
    private final BoardHeartRedisRepository boardHeartRedisRepository;

    public Board toBoard(Member member, BoardType boardType, String content) {
        return Board.builder()
                .writer(member)
                .boardType(boardType)
                .content(content)
                .commentCount(0)
                .heartCount(0)
                .isReport(Boolean.FALSE)
                .build();
    }

    //redis
    public BoardInfo toBoardInfo(Board board, Integer boardHeartsCount, Boolean isLiked, Boolean isMine) {
        return BoardInfo.builder()
                .boardId(board.getId())
                .writerId(board.getWriter().getId())
                .content(board.getContent())
                .heartCount(boardHeartsCount)
                .commentCount(board.getCommentCount())
                .createAt(board.getCreatedAt())
                .isLiked(isLiked)
                .isMine(isMine)
                .isReported(board.getIsReport())
                .build();
    }

    public SliceResponse<BoardInfo> toSliceBoardInfoForRedis(Member member, Slice<BoardInfoInterface> boardSlice) {
        return SliceResponse.<BoardInfo>builder()
                .number(boardSlice.getNumber())
                .size(boardSlice.getSize())
                .content(boardSlice.getContent().stream().map(boardInfoInterface -> {
                                    int heartCount = boardHeartRedisRepository.getBoardHeartsCount(boardInfoInterface.getBoard().getId());
                                    boolean isLiked = boardHeartRedisRepository.isMemberLikedBoard(member.getId(), boardInfoInterface.getBoard().getId());
                                    return toBoardInfo(boardInfoInterface.getBoard(), heartCount, isLiked, boardInfoInterface.getIsMine());
                                })
                                .toList()
                )
                .numberOfElements(boardSlice.getNumberOfElements())
                .hasPrevious(boardSlice.hasPrevious())
                .hasNext(boardSlice.hasNext())
                .build();
    }

    //rdb
    public BoardInfo toBoardInfo(Board board, Boolean isLiked, Boolean isMine) {
        return BoardInfo.builder()
                .boardId(board.getId())
                .writerId(board.getWriter().getId())
                .content(board.getContent())
                .heartCount(board.getHeartCount())
                .commentCount(board.getCommentCount())
                .createAt(board.getCreatedAt())
                .isLiked(isLiked)
                .isMine(isMine)
                .isReported(board.getIsReport())
                .build();
    }

    public SliceResponse<BoardInfo> toSliceBoardInfo(Slice<BoardInfoInterface> boardSlice) {
        return SliceResponse.<BoardInfo>builder()
                .number(boardSlice.getNumber())
                .size(boardSlice.getSize())
                .content(boardSlice.getContent().stream().map(boardInfoInterface ->
                        toBoardInfo(boardInfoInterface.getBoard(), boardInfoInterface.getIsLike(), boardInfoInterface.getIsMine()))
                        .toList()
                )
                .numberOfElements(boardSlice.getNumberOfElements())
                .hasPrevious(boardSlice.hasPrevious())
                .hasNext(boardSlice.hasNext())
                .build();
    }
}

