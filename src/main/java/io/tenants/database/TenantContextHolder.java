package io.tenants.database;

import io.tenants.database.admin.model.Tenant;
import lombok.experimental.UtilityClass;
import org.springframework.core.NamedInheritableThreadLocal;
import org.springframework.core.NamedThreadLocal;

@UtilityClass
public class TenantContextHolder {

    private static final ThreadLocal<TenantContext> tenants;

    private static final ThreadLocal<TenantContext> inheritableTenants;

    static {
        tenants = new NamedThreadLocal<>("tenants Context");
        inheritableTenants = new NamedInheritableThreadLocal<>("tenants Context");
    }

    public static void reset() {
        tenants.remove();
        inheritableTenants.remove();
    }

    public static TenantContext getContext() {
        TenantContext context = tenants.get();
        if (context == null) {
            context = inheritableTenants.get();
        }
        return context;
    }

    public static void setTenant(Tenant tenant) {
        TenantContext tenantContext = new TenantContext(tenant);
        setTenant(tenantContext);
    }

    public static void setTenant(Tenant tenant, boolean inheritable) {
        TenantContext tenantContext = new TenantContext(tenant);
        setTenant(tenantContext, inheritable);
    }

    public static void setTenant(TenantContext tenantContext) {
        setTenant(tenantContext, false);
    }

    public static void setTenant(TenantContext tenantContext, boolean inheritable) {
        if (tenantContext == null) {
            reset();
        } else {
            if (inheritable) {
                inheritableTenants.set(tenantContext);
                tenants.remove();
            } else {
                tenants.set(tenantContext);
                inheritableTenants.remove();
            }
        }
    }
}
