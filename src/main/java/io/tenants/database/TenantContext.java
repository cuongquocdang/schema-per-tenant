package io.tenants.database;

import io.tenants.database.admin.model.Tenant;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class TenantContext {
    private static final String TENANT = "tenant";

    private final Map<String, Object> context = new HashMap<>();

    public TenantContext(Tenant tenant) {
        context.put(TENANT, tenant);
    }

    public TenantContext(Long tenantId) {
        this(getTenant(tenantId));
    }

    private static Tenant getTenant(Long tenantId) {
        Tenant tenant = new Tenant();
        tenant.setId(tenantId);
        return tenant;
    }

    public Tenant getTenant() {
        return (Tenant) context.get(TENANT);
    }

    public Serializable getTenantId() {
        return getTenant().getId();
    }
}
