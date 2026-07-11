package com.qpoos.erp.common.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

/**
 * Central utility for reading the current authenticated user's identity.
 *
 * <p>Usage:</p>
 * <pre>{@code
 * UUID userId = SecurityUtils.getUserId();
 * }</pre>
 *
 * <p>All info is sourced from {@link UserPrincipal} which is
 * populated at authentication time <em>from JWT claims only</em> — no DB call.</p>
 */
public final class SecurityUtils {

    private SecurityUtils() {}


    /** Returns the full principal for the current request. Throws if not authenticated. */
    public static UserPrincipal getPrincipal() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal p) {
            return p;
        }
        throw new IllegalStateException("No authenticated user in current security context");
    }

    public static UUID getUserId() {
        return getPrincipal().id();
    }

    public static String getEmail() {
        return getPrincipal().email();
    }

    public static String getRole() {
        return getPrincipal().role();
    }

    /** May be {@code null} if the user has not selected a company yet. */
    public static UUID getCompanyId() {
        return getPrincipal().companyId();
    }


}
