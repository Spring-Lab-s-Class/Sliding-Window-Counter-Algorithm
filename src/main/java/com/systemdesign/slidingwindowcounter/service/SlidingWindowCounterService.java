package com.systemdesign.slidingwindowcounter.service;

import com.systemdesign.slidingwindowcounter.dto.response.SlidingWindowCounterProfileResponse;
import com.systemdesign.slidingwindowcounter.dto.response.SlidingWindowCounterResponse;
import com.systemdesign.slidingwindowcounter.exception.RateExceptionCode;
import com.systemdesign.slidingwindowcounter.exception.RateLimitExceededException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SlidingWindowCounterService {

    private final ReactiveRedisTemplate<String, Object> reactiveRedisTemplate;
    private final static String SLIDING_WINDOW_KEY = "SlidingWindowCounter:"; // 키
    private final static long SLIDING_WINDOW_COUNTER_MAX_REQUEST = 1000; // 최대 요청 허용 수
    private final static long SLIDING_WINDOW_COUNTER_DURATION = 60; // 60초

    public Mono<SlidingWindowCounterProfileResponse> createSlidingWindowCounter() {
        long currentTimestamp = System.currentTimeMillis();
        String redisKey = generateRedisKey("requests");
        log.info("Sliding Window Counter created. key: {}", redisKey);

        // 현재 윈도우의 시작 시간 계산
        double startTimeCurrentWindow = calculateTimeRange(currentTimestamp);

        //식별자 - 현재의 타임 스탬프와 UUID 조합
        String uniqueRequestIdentifier = String.valueOf(currentTimestamp) + ":" + UUID.randomUUID().toString();

        return reactiveRedisTemplate.opsForZSet()
                .count(redisKey, Range.closed(startTimeCurrentWindow, (double) currentTimestamp))
                .defaultIfEmpty(0L)
                .flatMap(previousCount -> reactiveRedisTemplate.opsForZSet()
                        .add(redisKey, uniqueRequestIdentifier, currentTimestamp)
                        .flatMap(success -> {
                            if (!success) {
                                return Mono.empty();
                            }

                            double overlapRate = calculateOverlapRate(currentTimestamp);

                            // 현재 요청을 포함한 최종 요청 수 계산
                            long totalCount;
                            if (previousCount == 0) {
                                totalCount = 1;
                                log.info("Init Sliding Window Counter count: {}", totalCount);
                            } else {
                                totalCount = Math.round(1 + (previousCount * overlapRate));
                                log.info("Adjusted Sliding Window Counter count: {}", totalCount);
                            }

                            log.info("Sliding Window Counter count: {}", totalCount);
                            if (totalCount >= SLIDING_WINDOW_COUNTER_MAX_REQUEST) {
                                log.error("Rate limit exceeded. key: {}", redisKey);
                                return Mono.error(
                                        new RateLimitExceededException(RateExceptionCode.COMMON_TOO_MANY_REQUESTS, totalCount)
                                );
                            } else {
                                return Mono.just(
                                        SlidingWindowCounterProfileResponse.from(
                                                List.of(SlidingWindowCounterResponse.from(redisKey, totalCount))
                                        )
                                );
                            }
                        }))
                // 에러 로깅
                .doOnError(error -> {
                    log.error("An error occurred: {}", error.getMessage());
                })
                .onErrorResume(error -> {
                    // 에러가 발생하면 RateLimitExceededException을 반환
                    return Mono.error(new RateLimitExceededException(RateExceptionCode.COMMON_TOO_MANY_REQUESTS, 0L));
                });
    }

    public Flux<SlidingWindowCounterResponse> findAllSlidingWindowCounter() {
        String redisKey = generateRedisKey("requests");
        long currentTimestamp = System.currentTimeMillis();
        log.info("Sliding Window Counter find all. key: {}", redisKey);

        return reactiveRedisTemplate.opsForZSet().rangeByScore(redisKey,
                        Range.closed(calculateTimeRange(currentTimestamp), (double) currentTimestamp))
                .map(value -> {
                    String[] parts = value.toString().split(":");
                    long timestamp = Long.parseLong(parts[0]);
                    log.info("Sliding Window Counter value: {}", timestamp);
                    return SlidingWindowCounterResponse.from(redisKey, timestamp);
                });
    }

    //이전 윈도와 현재 윈도에 겹치는 시간 비율 계산
    private double calculateOverlapRate(long currentTimestamp) {
        //이전 윈도우의 시작 시간
        double startTimePreviousWindow = calculateTimeRange(currentTimestamp - SLIDING_WINDOW_COUNTER_DURATION * 1000);

        // 현재 윈도우의 시작 시간
        double startTimeCurrentWindow = calculateTimeRange(currentTimestamp);

        // 겹치는 시간의 길이를 계산
        double overlapDuration = (startTimeCurrentWindow - startTimePreviousWindow) / 1000.0;

        // 겹치는 시간 비율을 계산
        double overlapRate = overlapDuration / SLIDING_WINDOW_COUNTER_DURATION;

        return overlapRate;
    }

    private double calculateTimeRange(long currentTimestamp) {
        return currentTimestamp - SLIDING_WINDOW_COUNTER_DURATION * 1000;
    }

    private String generateRedisKey(String requestType) {
        return SLIDING_WINDOW_KEY + requestType;
    }
}
