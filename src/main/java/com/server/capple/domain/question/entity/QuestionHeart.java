package com.server.capple.domain.question.entity;

import org.springframework.data.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("QuestionHeart")
public class QuestionHeart {
    @Id
    private Long questionId;
    private Long memberId;

}
