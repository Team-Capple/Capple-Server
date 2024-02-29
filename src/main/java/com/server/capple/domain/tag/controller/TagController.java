package com.server.capple.domain.tag.controller;

import com.server.capple.domain.tag.dto.TagResponse;
import com.server.capple.domain.tag.service.TagService;
import com.server.capple.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Tag(Keyword) API", description = "태그(키워드) API입니다.")
@RequestMapping("/tags")
public class TagController {
    private final TagService tagService;

    @Operation(summary = "키워드 검색 API", description = " 키워드 검색 API 입니다." +
            "requestparam 으로 keyword를 주세요." )
    @GetMapping("/search")
    public BaseResponse<TagResponse.TagInfos> searchTag(@RequestParam(name = "keyword") String keyword) {
        return BaseResponse.onSuccess(tagService.searchTags(keyword));
    }

}
