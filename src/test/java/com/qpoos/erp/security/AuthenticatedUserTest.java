package com.qpoos.erp.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AuthenticatedUserTest {

    @Test
    void readsIdEmailAndRoleFromAuthenticatedPrincipal() {
        UUID userId = UUID.randomUUID();
        AuthenticatedUserPrincipal principal = new AuthenticatedUserPrincipal(
                userId,
                "user@example.com",
                "user",
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                principal,
                null,
                principal.authorities()
        );

        assertThat(AuthenticatedUser.id(authentication)).isEqualTo(userId);
        assertThat(AuthenticatedUser.email(authentication)).isEqualTo("user@example.com");
        assertThat(AuthenticatedUser.role(authentication)).isEqualTo("user");
    }

    @Test
    void rejectsUnexpectedPrincipalType() {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                "not-a-principal",
                null,
                List.of()
        );

        assertThatThrownBy(() -> AuthenticatedUser.id(authentication))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Authenticated user is missing");
    }
}
