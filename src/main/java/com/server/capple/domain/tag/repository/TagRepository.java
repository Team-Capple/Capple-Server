package com.server.capple.domain.tag.repository;

import com.server.capple.domain.tag.entity.Tag;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag,Long> {
    Optional<Tag> findByTagName(String tagName);
    @Query("SELECT t FROM Tag t WHERE t.tagName LIKE %:keyword%")
    List<Tag> findTagsByKeyword(@Param("keyword") String keyword);
}
