<<<<<<<< Updated upstream:src/main/java/com/qpoos/erp/auth/infrastructure/token/ForgotPasswordTokenRepository.java
package com.qpoos.erp.auth.infrastructure.token;
========
package com.qpoos.erp.auth.repository.token;
>>>>>>>> Stashed changes:src/main/java/com/qpoos/erp/auth/repository/token/ForgotPasswordTokenRepository.java

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ForgotPasswordTokenRepository extends JpaRepository<ForgotPasswordToken, UUID> {
    Optional<ForgotPasswordToken> findByTokenHash(String tokenHash);
}
