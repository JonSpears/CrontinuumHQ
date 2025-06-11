package com.jonspears.schedulerservice.domain.entity;

import com.jonspears.sharedlibs.tenant.TenantContext;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "jobs")
public class Job{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(name = "tenant_id", nullable = false) // Critical for multi-tenancy
    private UUID tenantId;

    // Required by JPA
    protected Job() {}

    public Job(String name) {
        this.name = name;
        this.tenantId = UUID.fromString(TenantContext.getTenantId());
    }

    // Getters
    public UUID getId() { return id; }
    public String getName() { return name; }
    public UUID getTenantId() { return tenantId; }
}