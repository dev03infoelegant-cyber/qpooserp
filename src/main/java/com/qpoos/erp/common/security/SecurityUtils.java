package com.qpoos.erp.common.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

/**
 * Central utility for reading the current authenticated user's identity.
 *
 * <p>Two usage flavours:</p>
 * <ul>
 *   <li><b>Controller style</b> — pass the Spring-injected {@link Authentication} object:
 *     <pre>{@code
 *     public ResponseEntity<?> endpoint(Authentication auth) {
 *         UUID userId = SecurityUtils.getUserId(auth);
 *     }
 *     }</pre>
 *   </li>
 *   <li><b>Service / anywhere style</b> — read from {@link SecurityContextHolder} (no parameter):
 *     <pre>{@code
 *     UUID userId = SecurityUtils.getUserId();
 *     }</pre>
 *   </li>
 * </ul>
 *
 * <p>All info is sourced from {@link UserPrincipal} which is
 * populated at authentication time <em>from JWT claims only</em> — no DB call.</p>
 */
public final class SecurityUtils {

    private SecurityUtils() {}

    // ─── Context-holder flavour (use inside services / anywhere in the call stack) ──

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

    // ─── Authentication-parameter flavour (preferred in controllers) ─────────────

    public static UUID getUserId(Authentication auth) {
        return principal(auth).id();
    }

    public static String getEmail(Authentication auth) {
        return principal(auth).email();
    }

    public static String getRole(Authentication auth) {
        return principal(auth).role();
    }

    /** May be {@code null} if the user has not selected a company yet. */
    public static UUID getCompanyId(Authentication auth) {
        return principal(auth).companyId();
    }

    // ─── Internal ────────────────────────────────────────────────────────────────

    private static UserPrincipal principal(Authentication auth) {
        if (auth == null || !(auth.getPrincipal() instanceof UserPrincipal p)) {
            throw new IllegalStateException("Authenticated user principal is missing or of unexpected type");
        }
        return p;
    }
}
