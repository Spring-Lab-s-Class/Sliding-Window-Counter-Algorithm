package com.systemdesign.slidingwindowcounter.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SlidingWindowCounterProfileResponse {

    private List<SlidingWindowCounterResponse> counters;

    public static SlidingWindowCounterProfileResponse from(List<SlidingWindowCounterResponse> counters) {
        return SlidingWindowCounterProfileResponse.builder()
                .counters(counters)
                .build();
    }
}
