package com.server.capple.global.common;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;

import java.util.List;

@Getter
@NoArgsConstructor
public class SliceResponse<T> {
    int number;
    int size;
    List<T> content;
    int numberOfElements;
    boolean hasPrevious;
    boolean hasNext;

    public SliceResponse(Slice<T> sliceObject) {
        number = sliceObject.getNumber();
        size = sliceObject.getSize();
        content = sliceObject.getContent();
        numberOfElements = sliceObject.getNumberOfElements();
        hasPrevious = sliceObject.hasPrevious();
        hasNext = sliceObject.hasNext();
    }

    @Builder
    public SliceResponse(int number, int size, List<T> content, int numberOfElements, boolean hasPrevious, boolean hasNext) {
        this.number = number;
        this.size = size;
        this.content = content;
        this.numberOfElements = numberOfElements;
        this.hasPrevious = hasPrevious;
        this.hasNext = hasNext;
    }
}
