package com.intuit.urlshortner;

import com.intuit.urlshortner.service.redis.DistributedLockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

public class DistributedLockServiceTest {

    private DistributedLockService distributedLockService;

    @BeforeEach
    public void setup() {
        distributedLockService = new DistributedLockService();
    }


    @Test
    public void testReleaseLock() {
        distributedLockService.releaseLock("lock-key");
    }
}
