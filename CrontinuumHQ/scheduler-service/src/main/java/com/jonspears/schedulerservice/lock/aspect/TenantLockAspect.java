package com.jonspears.schedulerservice.lock.aspect;

import com.jonspears.schedulerservice.annotation.TenantLocked;
import com.jonspears.schedulerservice.exception.JobConcurrencyException;
import com.jonspears.schedulerservice.lock.RedisTenantLock;
import com.jonspears.schedulerservice.metrics.LockMetrics;
import com.jonspears.schedulerservice.util.SpelEvaluationUtils;
import com.jonspears.sharedlibs.tenant.TenantContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TenantLockAspect {
    @Autowired private RedisTenantLock lock;

    @Autowired private LockMetrics metrics;

    @Around("@annotation(tenantLocked)")
    public Object enforceLock(ProceedingJoinPoint pjp, TenantLocked tenantLocked) throws Throwable {
        String resourceId = resolveResourceId(pjp, tenantLocked);

        if (!lock.tryLock(resourceId)) {
            metrics.recordLockFailure(TenantContext.getTenantId());
            throw new JobConcurrencyException(resourceId);
        }

        try {
            return pjp.proceed();
        } finally {
            lock.releaseLock(resourceId);
        }
    }

    private String resolveResourceId(JoinPoint jp, TenantLocked tenantLocked) {
        // Supports SpEL expressions like "#job.id"
        return SpelEvaluationUtils.evaluate(tenantLocked.resourceId(), jp);
    }
}