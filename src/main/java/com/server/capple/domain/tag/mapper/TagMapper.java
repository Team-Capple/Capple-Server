package com.server.capple.domain.tag.mapper;

import com.server.capple.domain.tag.dto.TagResponse;
import com.server.capple.domain.tag.entity.Tag;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TagMapper {

    public TagResponse.TagInfos toTagInfos(List<Tag> tags) {

        List<String> tagNames = tags.stream()
                .map(Tag::getTagName)
                .toList();

        return TagResponse.TagInfos.builder()
                .tags(tagNames)
                .build();
    }
}
