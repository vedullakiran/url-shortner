package com.intuit.urlshortner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.intuit.urlshortner.service.LocalCacheCounterService;
import com.intuit.urlshortner.service.UniqueIdGeneratorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UniqueIdGeneratorServiceTest {

    private LocalCacheCounterService cacheCounterService;
    private UniqueIdGeneratorService uniqueIdGeneratorService;

    @BeforeEach
    public void setUp() {
        cacheCounterService = mock(LocalCacheCounterService.class);
        uniqueIdGeneratorService = new UniqueIdGeneratorService(cacheCounterService);
    }

    @Test
    public void testGetUniqueId() {
        // Mock the behavior of cacheCounterService
        when(cacheCounterService.getNextCounterId()).thenReturn(123L);
        when(cacheCounterService.getRangeStartId()).thenReturn(1000L);

        // Call the method to be tested
        Long result = uniqueIdGeneratorService.getUniqueId();

        // Verify the expected behavior
        assertEquals(1123L, result); // 123 + 1000
        verify(cacheCounterService, times(1)).getNextCounterId();
        verify(cacheCounterService, times(1)).getRangeStartId();
    }
}

