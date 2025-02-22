package com.server.capple.domain.member.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum AcademyGeneration {
    UNKNOWN("", -1),
    GENERATION_1("1기", 22),
    GENERATION_2("2기", 23),
    GENERATION_3("3기", 24),
    GENERATION_4("4기", 25),
    GENERATION_5("5기", 26),
    GENERATION_6("6기", 27),
    ;
    private final String generation;
    private final Integer year;

    private static final Map<Integer, AcademyGeneration> YEAR_TO_GENERATION = Stream.of(values()).collect(Collectors.toMap(AcademyGeneration::getYear, e -> e));
    public static AcademyGeneration generation(Integer year) {
        return YEAR_TO_GENERATION.getOrDefault(year, UNKNOWN);
    }
}
