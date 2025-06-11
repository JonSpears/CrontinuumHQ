package com.jonspears.schedulerservice.annotation;

import java.lang.annotation.*;
import org.springframework.core.annotation.AliasFor;

/**
 * Annotation to lock a method for a specific tenant.
 * This is used to ensure that only one instance of the method can be executed
 * for a given tenant at a time, preventing concurrency issues.
 * <p>
 * Usage: 1. Basic usage
 *              @TenantLocked("#job.id")
 *              public void executeJob(Job job) {...}
 * <p>
 *        2. Multiple parameters
 *              @TenantLocked("#job.tenantId + ':' + #job.name")
 *              public void logJob(Job job, boolean verbose) {...}
 * <p>
 *        3. Default fallback
 *              @TenantLocked (Uses "default-resource")
 *              public void systemTask() {...}
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TenantLocked {

    @AliasFor("resourceId")
    String value() default "";

    @AliasFor("value")
    String resourceId() default "";

}
