package com.jonspears.schedulerservice.lock;

import com.jonspears.schedulerservice.metrics.LockMetrics;
import com.jonspears.sharedlibs.tenant.TenantContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

@Component
public class RedisTenantLock {

    private static final long LOCK_TIMEOUT_MS = 30000; // 30 seconds
    private static final long REFRESH_INTERVAL_MS = 10000;

    private final StringRedisTemplate redisTemplate;
    private final LockMetrics lockMetrics;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public RedisTenantLock(StringRedisTemplate redisTemplate, LockMetrics lockMetrics) {
        this.redisTemplate = redisTemplate;
        this.lockMetrics = lockMetrics;
    }

    public boolean tryLock(String resourceKey) {
        String lockKey = getLockKey(resourceKey);
        boolean acquired = Boolean.TRUE.equals(
                redisTemplate.opsForValue().setIfAbsent(
                        lockKey,
                        "locked",
                        LOCK_TIMEOUT_MS,
                        TimeUnit.MILLISECONDS
                )
        );
        if (!acquired){
            lockMetrics.recordLockFailure(TenantContext.getTenantId());
        }else {

            scheduleLockRefresh(lockKey);
        }
        return acquired;
    }

    @Async
    protected void scheduleLockRefresh(String lockKey) {
        scheduler.scheduleAtFixedRate(
                () -> redisTemplate.expire(lockKey, LOCK_TIMEOUT_MS, TimeUnit.MILLISECONDS),
                REFRESH_INTERVAL_MS,
                REFRESH_INTERVAL_MS,
                TimeUnit.MILLISECONDS
        );
    }

    private String getLockKey(String resourceKey) {
        return String.format("lock:%s:%s", TenantContext.getTenantId(), resourceKey);
    }

    public void releaseLock(String resourceKey) {
        String tenantId = TenantContext.getTenantId();
        redisTemplate.delete(String.format("lock:%s:%s", tenantId, resourceKey));
    }
}