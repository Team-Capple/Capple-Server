package com.server.capple.dummy;

import com.server.capple.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "더미데이터 생성 API", description = "더미데이터 생성을 위한 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/dummy")
public class DummyController {

    private final DummyService dummyService;

    @Operation(summary = "멤버, 게시글,게시글 좋아요 더미 생성 API", description = "멤버, 게시글과 게시글 좋아요 더미를 생성합니다." +
            "생성하고싶은 멤버 수와, 게시글 수를 파라미터로 입력해주세요. 좋아요는 redis와 rdb에 동시에 저장됩니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
    })
    @PostMapping()
    private BaseResponse<Object> generateDummyBoards(@RequestParam("memberCount") int memberCount, @RequestParam("boardCount") int boardCount) {
        return BaseResponse.onSuccess(dummyService.generateDummy(memberCount, boardCount));
    }
}
