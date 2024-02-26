package com.server.capple.domain.question.controller;

import com.server.capple.domain.question.service.AdminQuestionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "관리자 질문 API", description = "관리자 질문 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/questions")
public class AdminQuestionController {
    private final AdminQuestionService adminQuestionService;

}
