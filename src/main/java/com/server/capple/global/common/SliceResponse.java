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
    int size;
    List<T> content;
    int numberOfElements;
    String threshold;
    boolean hasNext;

    public SliceResponse(Slice<T> sliceObject) {
        size = sliceObject.getSize();
        content = sliceObject.getContent();
        numberOfElements = sliceObject.getNumberOfElements();
        hasNext = sliceObject.hasNext();
    }

    @Builder
    public SliceResponse(int size, List<T> content, int numberOfElements, String threshold, boolean hasNext, Integer total) {
        this.size = size;
        this.content = content;
        this.numberOfElements = numberOfElements;
        this.threshold = threshold;
        this.hasNext = hasNext;
        this.total = total;
    }

    /**
     * P : 데이터베이스에서 반환 받은 데이터 타입<BR>
     * R : 사용자게에 반환할 데이터 타입
     */
    public static <P, R> SliceResponse<R> toSliceResponse(Slice<P> sliceObject, List<R> content, String threshold, Integer total) {
        return SliceResponse.<R>builder()
            .size(sliceObject.getSize())
            .content(content)
            .numberOfElements(sliceObject.getNumberOfElements())
            .threshold(threshold)
            .hasNext(sliceObject.hasNext())
            .total(total)
            .build();
    }
}
