package com.server.capple.domain.tag.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public class TagResponse {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class TagInfos {
        List<String> tags = new ArrayList<>();
    }
}
