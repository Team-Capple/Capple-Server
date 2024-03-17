package com.server.capple.domain.tag.controller;

import com.server.capple.domain.tag.dto.TagResponse;
import com.server.capple.domain.tag.service.TagService;
import com.server.capple.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Tag(Keyword) API", description = "태그(키워드) API입니다.")
@RequestMapping("/tags")
public class TagController {
    private final TagService tagService;

    @Operation(summary = "키워드 검색 API", description = " 키워드 검색 API 입니다." +
            "request param 으로 keyword를 주세요." )
    @GetMapping("/search")
    public BaseResponse<TagResponse.TagInfos> searchTag(@RequestParam(name = "keyword") String keyword) {
        return BaseResponse.onSuccess(tagService.searchTags(keyword));
    }

    @Operation(summary = "이 질문 답변에 사람들이 많이 쓴 키워드 조회 API", description = " 이 질문에 대해 사람들이 많이 쓴 키워드를 조회합니다." +
            "path variable 으로 questionId를 주세요." )
    @GetMapping("/{questionId}")
    public BaseResponse<TagResponse.TagInfos> getPopularTagsByQuestion(@PathVariable(name = "questionId") Long questionId) {
        return BaseResponse.onSuccess(tagService.getTagsByQuestion(questionId));
    }


    @Operation(summary = "사람들이 많이 쓴 키워드 조회 API", description = "질문에 상관없이 인기 키워드를 조회합니다.")
    @GetMapping()
    public BaseResponse<TagResponse.TagInfos> getPopularTags() {
        return BaseResponse.onSuccess(tagService.getPopularTags());
    }

}
