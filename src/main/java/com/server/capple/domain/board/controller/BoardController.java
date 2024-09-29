package com.server.capple.domain.board.controller;

import com.server.capple.config.security.AuthMember;
import com.server.capple.domain.board.dto.BoardRequest;
import com.server.capple.domain.board.dto.BoardResponse.BoardId;
import com.server.capple.domain.board.dto.BoardResponse.BoardInfo;
import com.server.capple.domain.board.dto.BoardResponse.ToggleBoardHeart;
import com.server.capple.domain.board.entity.BoardType;
import com.server.capple.domain.board.service.BoardService;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.global.common.BaseResponse;
import com.server.capple.global.common.SliceResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Tag(name = "게시판 API", description = "게시판 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;

    @Operation(summary = "게시글 생성 API", description = "게시글을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
    })
    @PostMapping()
    private BaseResponse<BoardId> createBoard(@AuthMember Member member,
                                              @RequestBody @Valid BoardRequest.BoardCreate request) {
        return BaseResponse.onSuccess(boardService.createBoard(member, request.getBoardType(), request.getContent()));
    }

    @Operation(summary = "카테고리별 게시글 조회 with REDIS API(프론트 사용 X, 성능 테스트 용)", description = "카테고리별 게시글을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
    })
    @GetMapping("/redis")
    private BaseResponse<SliceResponse<BoardInfo>> getBoardsByBoardTypeWithRedis(
            @AuthMember Member member,
            @RequestParam(name = "boardType", required = false) BoardType boardType,
            @Parameter(description = "조회 기준 시각")
            @RequestParam(required = false) LocalDateTime thresholdDate,
            @RequestParam(defaultValue = "0", required = false) Integer pageNumber, @RequestParam(defaultValue = "1000", required = false) Integer pageSize
    ) {
        return BaseResponse.onSuccess(boardService.getBoardsByBoardTypeWithRedis(member, boardType, thresholdDate, PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"))));
    }

    @Operation(summary = "카테고리별 게시글 조회", description = "카테고리별 게시글을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
    })
    @GetMapping()
    private BaseResponse<SliceResponse<BoardInfo>> getBoardsByBoardType(
            @AuthMember Member member,
            @RequestParam(name = "boardType", required = false) BoardType boardType,
            @Parameter(description = "조회 기준 시각")
            @RequestParam(required = false) LocalDateTime thresholdDate,
            @RequestParam(defaultValue = "0", required = false) Integer pageNumber, @RequestParam(defaultValue = "1000", required = false) Integer pageSize
    ) {
        return BaseResponse.onSuccess(boardService.getBoardsByBoardType(member, boardType, thresholdDate, PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"))));
    }

    @Operation(summary = "게시글 검색 API", description = "게시글을 검색합니다. 자유게시판에서만 검색이 가능합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
    })
    @GetMapping("/search")
    private BaseResponse<SliceResponse<BoardInfo>> searchBoardsByKeyword(
            @AuthMember Member member, @RequestParam(name = "keyword") String keyword,
            @Parameter(description = "조회 기준 시각")
            @RequestParam(required = false) LocalDateTime thresholdDate,
            @RequestParam(defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(defaultValue = "1000", required = false) Integer pageSize) {
        return BaseResponse.onSuccess(boardService.searchBoardsByKeyword(member, keyword, thresholdDate, PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"))));
    }


    @Operation(summary = "게시글 삭제 API", description = "게시글을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
    })
    @DeleteMapping("/{boardId}")
    private BaseResponse<BoardId> deleteBoard(@AuthMember Member member, @PathVariable(name = "boardId") Long boardId) {
        return BaseResponse.onSuccess(boardService.deleteBoard(member, boardId));
    }

    @Operation(summary = "게시글 좋아요/취소 API", description = " 게시글 좋아요/취소 API 입니다." +
            "pathVariable 으로 boardId를 주세요.")
    @PostMapping("/{boardId}/heart")
    public BaseResponse<ToggleBoardHeart> toggleBoardHeart(@AuthMember Member member, @PathVariable(value = "boardId") Long boardId) {
        return BaseResponse.onSuccess(boardService.toggleBoardHeart(member, boardId));
    }
}
