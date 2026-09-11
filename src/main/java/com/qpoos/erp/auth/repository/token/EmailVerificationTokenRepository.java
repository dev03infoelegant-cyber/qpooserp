<<<<<<<< Updated upstream:src/main/java/com/qpoos/erp/auth/infrastructure/token/EmailVerificationTokenRepository.java
package com.qpoos.erp.auth.infrastructure.token;
========
package com.qpoos.erp.auth.repository.token;
>>>>>>>> Stashed changes:src/main/java/com/qpoos/erp/auth/repository/token/EmailVerificationTokenRepository.java

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, UUID> {
    Optional<EmailVerificationToken> findByTokenHash(String tokenHash);
}
