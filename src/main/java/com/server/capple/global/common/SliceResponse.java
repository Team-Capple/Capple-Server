package com.server.capple.global.common;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;

import java.util.List;

@Getter
@NoArgsConstructor
public class SliceResponse<T> {
    Integer total;
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
    public SliceResponse(int number, int size, List<T> content, int numberOfElements, boolean hasPrevious, boolean hasNext, Integer total) {
        this.number = number;
        this.size = size;
        this.content = content;
        this.numberOfElements = numberOfElements;
        this.hasPrevious = hasPrevious;
        this.hasNext = hasNext;
        this.total = total;
    }

    /**
     * P : 데이터베이스에서 반환 받은 데이터 타입<BR>
     * R : 사용자게에 반환할 데이터 타입
     */
    public static <P, R> SliceResponse<R> toSliceResponse(Slice<P> sliceObject, List<R> content, Integer total) {
        return SliceResponse.<R>builder()
            .number(sliceObject.getNumber())
            .size(sliceObject.getSize())
            .content(content)
            .numberOfElements(sliceObject.getNumberOfElements())
            .hasPrevious(sliceObject.hasPrevious())
            .hasNext(sliceObject.hasNext())
            .total(total)
            .build();
    }
}
