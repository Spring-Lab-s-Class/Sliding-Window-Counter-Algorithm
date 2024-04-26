package com.systemdesign.slidingwindowcounter.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SlidingWindowCounterResponse {

    private String key;
    private Long requestCount;

    public static SlidingWindowCounterResponse from(String key, Long requestCount) {
        return SlidingWindowCounterResponse.builder()
                .key(key)
                .requestCount(requestCount)
                .build();
    }
}
