package com.jonspears.schedulerservice.tenant;

import com.jonspears.schedulerservice.domain.entity.Job;
import com.jonspears.schedulerservice.repositotry.JobRepository;
import com.jonspears.schedulerservice.util.SpelEvaluationUtils;
import com.jonspears.sharedlibs.tenant.TenantContext;
import org.aspectj.lang.JoinPoint;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@SpringBootTest
class MultiTenantRepositoryTest {

    @Autowired
    private JobRepository jobRepository;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("scheduler_test");

    private final JoinPoint someJoinPoint = null; // Mock or inject as needed for SpEL evaluation


    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
    }

    @Test
    void shouldIsolateTenants() {
        TenantContext.setTenantId("tenant_a");
        jobRepository.save(new Job("job1"));

        TenantContext.setTenantId("tenant_b");
        assertThat(jobRepository.findAll()).isEmpty();
    }

    @Test
    void testSpelEvaluation() {
        Job testJob = new Job("test-123");
        String result = SpelEvaluationUtils.evaluate("#job.id", someJoinPoint);
        assertEquals(testJob.getId().toString(), result);
    }
}
