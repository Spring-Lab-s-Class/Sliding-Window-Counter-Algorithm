package com.systemdesign.slidingwindowcounter.controller;

import com.systemdesign.slidingwindowcounter.dto.response.SlidingWindowCounterProfileResponse;
import com.systemdesign.slidingwindowcounter.dto.response.SlidingWindowCounterResponse;
import com.systemdesign.slidingwindowcounter.service.SlidingWindowCounterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("sliding-window-counter")
public class SlidingWindowCounterController {

    private final SlidingWindowCounterService slidingWindowCounterService;

    @GetMapping
    public Mono<ResponseEntity<Flux<SlidingWindowCounterResponse>>> findAllSlidingWindowLog() {
        return Mono.just(
                ResponseEntity.status(OK)
                        .body(slidingWindowCounterService.findAllSlidingWindowCounter())
        );
    }

    @PostMapping
    public Mono<ResponseEntity<SlidingWindowCounterProfileResponse>> createSlidingWindowCounter() {
        return slidingWindowCounterService.createSlidingWindowCounter()
                .map(response -> ResponseEntity.status(CREATED).body(response));
    }
}
