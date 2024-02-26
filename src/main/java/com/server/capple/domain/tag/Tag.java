package com.server.capple.domain.tag;

import org.springframework.data.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@RedisHash("tag")
public class Tag {
    @Id
    private Long id;

    private String name;
    private int usageCount;
    private Long answerId;
    private Long questionId;
    private LocalDateTime createAt;
}
