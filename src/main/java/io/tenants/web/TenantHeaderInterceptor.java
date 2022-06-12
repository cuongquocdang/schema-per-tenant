package io.tenants.web;

import io.tenants.database.TenantContextHolder;
import io.tenants.database.admin.model.Tenant;
import io.tenants.database.admin.repository.TenantRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class TenantHeaderInterceptor implements HandlerInterceptor {

    private static final String TENANT_HEADER = "X-Tenant-Id";

    private final TenantRepository tenantRepository;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) throws IOException {
        String tenantHeader = request.getHeader(TENANT_HEADER);
        boolean tenantSet = false;
        if (!tenantHeader.isEmpty()) {
            Tenant tenant = tenantRepository.findByName(tenantHeader)
                    .orElseThrow(() -> new RuntimeException("tenants not found"));
            TenantContextHolder.setTenant(tenant, true);
            tenantSet = true;
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write("{\"error\": \"No admin supplied\"}");
            response.getWriter().flush();
        }
        return tenantSet;
    }

    @Override
    public void postHandle(@NonNull HttpServletRequest request,
                           @NonNull HttpServletResponse response,
                           @NonNull Object handler,
                           ModelAndView modelAndView) {
        TenantContextHolder.reset();
    }


}
