package com.jonspears.schedulerservice.repositotry;

import com.jonspears.schedulerservice.domain.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JobRepository extends JpaRepository<Job, UUID> {
    List<Job> findByTenantId(UUID tenantId); // Custom query for tenant isolation
}
