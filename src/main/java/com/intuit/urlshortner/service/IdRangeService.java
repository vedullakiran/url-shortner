package com.intuit.urlshortner.service;

import com.intuit.urlshortner.entities.IdRange;
import com.intuit.urlshortner.exceptions.LockAcquisitionException;
import com.intuit.urlshortner.repository.IdRangeRepository;
import com.intuit.urlshortner.service.redis.DistributedLockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.intuit.urlshortner.utils.ConstantUtils.*;
import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class IdRangeService {

    private final IdRangeRepository idRangeRepository; // Repository for storing used ranges
    private final DistributedLockService redisLockUtil; // Your distributed lock provider

    public IdRange getAndAddNewRange() {
        boolean locked = redisLockUtil.acquireLock(RANGE_OPERATION_LOCK_KEY);

        if (locked) {
            try {
                Long lastStartId = idRangeRepository.findLastStartId();
                IdRange newRange = new IdRange().setStartId(INITIAL_RANGE_START);
                if (nonNull(lastStartId)) {
                    newRange.setStartId(lastStartId + COUNTER_LIMIT);
                }
                idRangeRepository.save(newRange);

                return newRange;
            } finally {
                redisLockUtil.releaseLock(RANGE_OPERATION_LOCK_KEY);
            }
        } else {
            throw new LockAcquisitionException("Failed to acquire lock for range operation");
        }
    }
}
