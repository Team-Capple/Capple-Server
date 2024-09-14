package com.server.capple.global.common;

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
}
