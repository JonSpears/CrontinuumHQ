package com.jonspears.schedulerservice.repository;

import com.jonspears.schedulerservice.domain.entity.Job;
import com.jonspears.schedulerservice.repositotry.JobRepository;
import com.jonspears.sharedlibs.tenant.TenantContext;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JobRepositoryIT {
    private final UUID TENANT_A = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
    private final UUID TENANT_B = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479");

    @Autowired
    private JobRepository jobRepo;

    @BeforeEach
    void setup() {
        jobRepo.deleteAll();
    }

    @Test
    void shouldIsolateTenants() {
        // Tenant A's data
        TenantContext.setTenantId(TENANT_A.toString());
        jobRepo.save(new Job("cron_job_1"));

        // Tenant B's perspective
        TenantContext.setTenantId(TENANT_B.toString());
        assertThat(jobRepo.findAll()).isEmpty();

        // Verify tenantA's data exists
        TenantContext.setTenantId(TENANT_A.toString());
        assertThat(jobRepo.findAll())
                .hasSize(1)
                .first()
                .extracting(Job::getName)
                .isEqualTo("cron_job_1");
    }
}