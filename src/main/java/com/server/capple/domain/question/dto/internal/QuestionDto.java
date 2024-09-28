package com.server.capple.domain.question.dto.internal;

import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.ToString;

public class QuestionDto {
    @Getter
    @ToString
    public static class QuestionInsertDto {
        @CsvBindByName(column = "number")
        private Long questionId;
        @CsvBindByName(column = "question")
        private String questionContent;
    }
}
