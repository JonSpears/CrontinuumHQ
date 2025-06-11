package com.jonspears.schedulerservice.service;

import com.jonspears.schedulerservice.annotation.TenantLocked;
import com.jonspears.schedulerservice.domain.entity.Job;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class JobService {

    @TenantLocked(resourceId = "#job.id")
    public void executeJob(@NonNull Job job) {
        // Lock acquired automatically
        // Metrics recorded on failure
        // Lock auto-refreshed in background
    }

}
