package com.qpoos.erp.dto.auth;

import java.util.UUID;

public record MeResponse(UUID id, String email, String role, boolean emailVerified) {
}
