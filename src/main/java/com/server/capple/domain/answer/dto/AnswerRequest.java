package com.server.capple.domain.answer.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AnswerRequest {

    private String answer;
    private List<String> keywords = new ArrayList<>();
}