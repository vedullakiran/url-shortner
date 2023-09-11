package com.intuit.urlshortner;

import com.intuit.urlshortner.entities.IdRange;
import com.intuit.urlshortner.exceptions.LockAcquisitionException;
import com.intuit.urlshortner.repository.IdRangeRepository;
import com.intuit.urlshortner.service.IdRangeService;
import com.intuit.urlshortner.service.redis.DistributedLockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import static com.intuit.urlshortner.utils.ConstantUtils.COUNTER_LIMIT;
import static com.intuit.urlshortner.utils.ConstantUtils.INITIAL_RANGE_START;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class IdRangeServiceTest {

    private static final String RANGE_OPERATION_LOCK_KEY = "range-lock-key";

    private IdRangeRepository idRangeRepository;
    private DistributedLockService redisLockUtil;

    @InjectMocks
    private IdRangeService idRangeService;

    @BeforeEach
    public void setUp() {
        idRangeRepository = mock(IdRangeRepository.class);
        redisLockUtil = mock(DistributedLockService.class);
        idRangeService = new IdRangeService(idRangeRepository, redisLockUtil);
    }

    @Test
    public void testGetAndAddNewRange_Success() {
        // Mocking
        Long start = 2000L;
        when(redisLockUtil.releaseLock(anyString())).thenReturn(Boolean.TRUE);
        when(redisLockUtil.acquireLock(anyString())).thenReturn(Boolean.TRUE);
        when(idRangeRepository.findLastStartId()).thenReturn(2000L);
        when(idRangeRepository.save(any(IdRange.class))).thenReturn(new IdRange());


        // Execution
        IdRange newRange = idRangeService.getAndAddNewRange();

        // Assertion
        assertNotNull(newRange);
        assertEquals(start + COUNTER_LIMIT, newRange.getStartId());
        verify(redisLockUtil, times(1)).releaseLock(anyString());
    }

    @Test
    public void testGetAndAddNewRange_FirstRange() {
        // Mocking
        when(redisLockUtil.releaseLock(anyString())).thenReturn(Boolean.TRUE);
        when(redisLockUtil.acquireLock(anyString())).thenReturn(true);
        when(idRangeRepository.findLastStartId()).thenReturn(null);
        when(idRangeRepository.save(any(IdRange.class))).thenReturn(new IdRange());

        // Execution
        IdRange newRange = idRangeService.getAndAddNewRange();

        // Assertion
        assertNotNull(newRange);
        assertEquals(INITIAL_RANGE_START, newRange.getStartId());
        verify(redisLockUtil, times(1)).releaseLock(anyString());
    }

    @Test
    public void testGetAndAddNewRange_LockAcquisitionFailed() {
        // Mocking
        when(redisLockUtil.acquireLock(anyString())).thenReturn(false);

        // Assertion
        assertThrows(LockAcquisitionException.class, () -> idRangeService.getAndAddNewRange());
        verify(redisLockUtil, never()).releaseLock(anyString());
    }
}

