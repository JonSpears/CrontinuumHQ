package com.jonspears.sharedlibs.tenant;

public class TenantContext {
    private static final ThreadLocal<String> currentTenant = new ThreadLocal<>();

    // Called by gateway-service during JWT parsing
    public static void setTenantId(String tenantId) {
        if (tenantId == null) {
            clear();
        } else {
            currentTenant.set(tenantId);
        }
    }

    // Used by scheduler-service data layer
    public static String getTenantId() {
        return currentTenant.get();
    }

    // Called at end of HTTP request/event processing
    public static void clear() {
        currentTenant.remove();
    }
}