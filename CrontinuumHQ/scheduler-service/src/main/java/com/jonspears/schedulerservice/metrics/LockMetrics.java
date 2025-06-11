package com.jonspears.schedulerservice.metrics;

import io.micrometer.core.instrument.*;
import org.springframework.stereotype.Component;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class LockMetrics {
    private final MeterRegistry meterRegistry;
    private final ConcurrentHashMap<String, Counter> tenantLockCounters = new ConcurrentHashMap<>();

    public LockMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public void recordLockFailure(String tenantId) {
        Counter counter = tenantLockCounters.computeIfAbsent(
                tenantId,
                id -> Counter.builder("crontinuum.lock.failures")
                        .tag("tenant", id)
                        .register(meterRegistry)
        );
        counter.increment();
    }
}
