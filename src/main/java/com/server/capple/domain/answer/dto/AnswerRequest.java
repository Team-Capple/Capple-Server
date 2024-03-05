package com.server.capple.domain.answer.dto;


import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerRequest {

    @NotEmpty
    private String answer;

    @Builder.Default
    private List<String> tags = new ArrayList<>();

}
