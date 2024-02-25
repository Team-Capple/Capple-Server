package com.server.capple.domain.answer.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("AnswerHeart")
public class AnswerHeart {
    @Id
    private Long answerId;
    private Long memberId;

}
