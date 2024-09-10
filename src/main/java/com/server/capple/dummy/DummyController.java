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

    @Operation(summary = "게시글,게시글 좋아요 더미 생성 API", description = "게시글과 게시글 좋아요 더미를 생성합니다." +
            "멤버 더미 먼저 생성 후 실행해주세요.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
    })
    @PostMapping("/board")
    private BaseResponse<Object> generateDummyBoards(@RequestParam("num") int num) {
        return BaseResponse.onSuccess(dummyService.generateDummyBoards(num));
    }

    @Operation(summary = "멤버 더미 생성 API", description = "멤버 더미를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
    })
    @PostMapping("/member")
    private BaseResponse<Object> generateDummyMembers() {
        return BaseResponse.onSuccess(dummyService.generateDummyMembers());
    }
}
