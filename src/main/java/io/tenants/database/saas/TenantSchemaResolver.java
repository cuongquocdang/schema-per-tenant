package io.tenants.database.saas;

import io.tenants.database.TenantContext;
import io.tenants.database.TenantContextHolder;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class TenantSchemaResolver implements CurrentTenantIdentifierResolver {

    public static final String DEFAULT_SCHEMA = "saas";

    @Override
    public String resolveCurrentTenantIdentifier() {
        TenantContext context = TenantContextHolder.getContext();
        if (context != null && context.getTenant() != null && StringUtils.hasLength(context.getTenant().getSchemaName())) {
            return context.getTenant().getSchemaName();
        } else {
            return DEFAULT_SCHEMA;
        }
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}
