<<<<<<<< Updated upstream:src/main/java/com/qpoos/erp/auth/infrastructure/token/RefreshTokenRepository.java
package com.qpoos.erp.auth.infrastructure.token;
========
package com.qpoos.erp.auth.repository.token;
>>>>>>>> Stashed changes:src/main/java/com/qpoos/erp/auth/repository/token/RefreshTokenRepository.java

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    Optional<RefreshToken> findByTokenHash(String tokenHash);
    List<RefreshToken> findAllByUserIdAndRevokedAtIsNull(UUID userId);
}
