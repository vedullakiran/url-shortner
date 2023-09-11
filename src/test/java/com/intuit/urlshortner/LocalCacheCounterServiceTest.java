package com.intuit.urlshortner;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.intuit.urlshortner.entities.IdRange;
import com.intuit.urlshortner.service.IdRangeService;
import com.intuit.urlshortner.service.LocalCacheCounterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class LocalCacheCounterServiceTest {

    private static final String CURRENT_RANGE_START = "currentRangeStart";
    private static final int CACHE_SIZE = 10000;
    private static final long COUNTER_LIMIT = 10000L;

    private LocalCacheCounterService cacheCounterService;

    private Cache<String, Long> counterCache;
    @Mock
    private IdRangeService idRangeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(idRangeService.getAndAddNewRange()).thenReturn(new IdRange().setStartId(0L));
        counterCache = CacheBuilder.newBuilder()
                .maximumSize(CACHE_SIZE)
                .build();
        cacheCounterService = new LocalCacheCounterService(idRangeService);
    }

    @Test
    public void testGetNextCounterId_NoLimitExceeded() {
        when(idRangeService.getAndAddNewRange()).thenReturn(new IdRange().setStartId(0L));
        cacheCounterService.loadCacheWithInitialValue();
        assertEquals(1L, cacheCounterService.getNextCounterId());
        assertEquals(2L, cacheCounterService.getNextCounterId());
    }

    @Test
    public void testGetNextCounterId_LimitExceeded() {
        when(idRangeService.getAndAddNewRange()).thenReturn(new IdRange().setStartId(0L));
        cacheCounterService.loadCacheWithInitialValue();
        while(cacheCounterService.getNextCounterId()<COUNTER_LIMIT) {
        }
        assertEquals(1L, cacheCounterService.getNextCounterId()); // Counter should reset to 1
    }

    @Test
    public void testHandleLimitExceeded_Success() {
        when(idRangeService.getAndAddNewRange()).thenReturn(new IdRange().setStartId(0L));
        cacheCounterService.loadCacheWithInitialValue();
        cacheCounterService.handleLimitExceeded();
        assertEquals(2L, cacheCounterService.getNextCounterId());
    }

    @Test
    public void testHandleLimitExceeded_ConcurrentAccess() {
        when(idRangeService.getAndAddNewRange()).thenReturn(new IdRange().setStartId(0L));
        cacheCounterService.loadCacheWithInitialValue();
        Runnable concurrentTask = () -> {
            cacheCounterService.handleLimitExceeded();
            cacheCounterService.getNextCounterId();
        };

        Thread thread1 = new Thread(concurrentTask);
        Thread thread2 = new Thread(concurrentTask);

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(3L, cacheCounterService.getNextCounterId());
    }

    @Test
    public void testGetRangeStartId() {
        when(idRangeService.getAndAddNewRange()).thenReturn(new IdRange().setStartId(0L));
        cacheCounterService.loadCacheWithInitialValue();
        assertEquals(0L, cacheCounterService.getRangeStartId());
    }

}
