package com.intuit.urlshortner.service;


import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.intuit.urlshortner.entities.IdRange;
import com.intuit.urlshortner.exceptions.LockAcquisitionException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.intuit.urlshortner.utils.ConstantUtils.*;


@Service
@RequiredArgsConstructor
public class LocalCacheCounterService {

    private final Lock limitExceededLock = new ReentrantLock();

    private final IdRangeService idRangeService;
    private Cache<String, Long> counterCache;
    private AtomicLong counter;

    @PostConstruct
    public void loadCacheWithInitialValue() {
        counterCache = CacheBuilder.newBuilder()
                .maximumSize(CACHE_SIZE)
                .build();
        IdRange idRange = idRangeService.getAndAddNewRange();
        counterCache.put(CURRENT_RANGE_START, idRange.getStartId());
        counter = new AtomicLong(0);
    }

    public Long getNextCounterId() {
        long nextCounterId = counter.incrementAndGet();
        if (nextCounterId > COUNTER_LIMIT) {
            handleLimitExceeded();
        }
        return counter.get();
    }

    public void handleLimitExceeded() {
        if (limitExceededLock.tryLock()) {
            try {
                // Check again if the limit is still exceeded (since another thread might have handled it already)
                long nextId = counter.incrementAndGet();
                if (nextId > COUNTER_LIMIT) {
                    IdRange idRange = idRangeService.getAndAddNewRange();
                    counterCache.put(CURRENT_RANGE_START, idRange.getStartId());
                    counter = new AtomicLong(1);
                }
            } finally {
                limitExceededLock.unlock();
            }
        } else {
            throw new LockAcquisitionException("unable to acquire lock");
        }
    }

    public Long getRangeStartId() {
        return counterCache.getIfPresent(CURRENT_RANGE_START);
    }

}
