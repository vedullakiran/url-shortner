package com.intuit.urlshortner.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UniqueIdGeneratorService {

    private final LocalCacheCounterService cacheCounterService;

    public Long getUniqueId() {
        return cacheCounterService.getNextCounterId() + cacheCounterService.getRangeStartId();
    }

}
