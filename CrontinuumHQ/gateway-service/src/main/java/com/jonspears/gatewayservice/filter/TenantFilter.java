package com.jonspears.gatewayservice.filter;

import com.jonspears.sharedlibs.tenant.TenantContext;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;

public class TenantFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        try {
            String tenantId = extractTenantFromJWT(req.getHeader("Authorization"));
            TenantContext.setTenantId(tenantId);
            chain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }

    private String extractTenantFromJWT(String authHeader) {
        // Implementation using jjwt or similar
        return "";
    }
}
