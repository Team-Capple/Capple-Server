package com.server.capple.domain.board.controller;

import com.server.capple.config.security.AuthMember;
import com.server.capple.domain.board.dto.BoardRequest;
import com.server.capple.domain.board.dto.BoardResponse;
import com.server.capple.domain.board.entity.BoardType;
import com.server.capple.domain.board.service.BoardService;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.question.dto.response.QuestionResponse;
import com.server.capple.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@Tag(name = "게시판 API", description = "게시판 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;

    @Operation(summary = "게시판 생성 API", description = "게시판을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
    })
    @PostMapping()
    private BaseResponse<BoardResponse.BoardCreate> createBoard(
            @AuthMember Member member,
            @RequestBody BoardRequest.BoardCreate request
    ) {
        return BaseResponse.onSuccess(boardService.createBoard(member, request.getBoardType(), request.getContent()));
    }

    @Operation(summary = "카테고리별 게시판 조회 API", description = "카테고리별 게시판을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
    })
    @GetMapping()
    private BaseResponse<BoardResponse.BoardsGetByBoardType> getBoardsByBoardType(
            @RequestParam(name = "boardType", required = false) BoardType boardType
            // TODO: 페이징 프론트 이슈로 추후 구현
//            @PageableDefault(sort = "created_at", direction = Sort.Direction.DESC) @Parameter(hidden = true) Pageable pageable
            ) {
        return BaseResponse.onSuccess(boardService.getBoardsByBoardType(boardType));
    }

}
