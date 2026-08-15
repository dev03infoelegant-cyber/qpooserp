package com.qpoos.erp.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.UUID;

public final class SecurityUtils {

    private SecurityUtils() {} // Utility class constructor

    public static AuthenticatedUserPrincipal getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof AuthenticatedUserPrincipal principal) {
            return principal;
        }
        throw new IllegalStateException("No authenticated user context found for the current request");
    }

    /**
     * Equivalent to your .NET: User.GetUserId()
     */
    public static UUID getUserId() {
        return getPrincipal().getUserId();
    }

    /**
     * Equivalent to your .NET: User.GetCompanyId()
     */
    public static UUID getCompanyId() {
        return getPrincipal().getCompanyId();
    }
}