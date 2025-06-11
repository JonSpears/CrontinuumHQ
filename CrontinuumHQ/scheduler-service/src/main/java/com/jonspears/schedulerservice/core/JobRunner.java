package com.jonspears.schedulerservice.core;

import com.jonspears.schedulerservice.annotation.TenantLocked;
import com.jonspears.schedulerservice.domain.entity.Job;
import com.jonspears.schedulerservice.lock.RedisTenantLock;
import com.jonspears.sharedlibs.tenant.TenantContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.stereotype.Service;

@Service
public class JobRunner {
    private static final Logger log = LoggerFactory.getLogger(JobRunner.class);
    private final RedisTenantLock lockManager;

    public JobRunner(RedisTenantLock lockManager) {
        this.lockManager = lockManager;
    }

    @TenantLocked(resourceId = "#job.id")
    public void execute(Job job) throws JobExecutionException {
        if (TenantContext.getTenantId() == null) {
            throw new IllegalStateException("No tenant context available");
        }

        try {
            log.info("Job {} has been acquired", job.getId());
            // Simulate job execution
            executeJobLogic(job);
            log.info("Executing job {} for tenant {}", job.getId(), job.getTenantId());

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Job execution interrupted for job {}", job.getId());
            throw new JobExecutionException("Job interrupted", e);
        } catch (Exception e) {
            log.error("Unexpected error during job execution for job {}", job.getId());
            throw new JobExecutionException("Job failed", e);
        }
    }

    private void executeJobLogic(Job job) throws InterruptedException {
        // Simulate job execution
        Thread.sleep(2000);
    }
}