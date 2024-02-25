package com.server.capple.domain.tag;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tag {

    private String name;
    private int usageCount;
    private String answerId;
    private String questionId;
    private LocalDateTime createAt;
}
