package com.qpoos.erp.repository;

import com.qpoos.erp.entity.ForgotPasswordTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ForgotPasswordTokenRepository extends JpaRepository<ForgotPasswordTokenEntity, UUID> {
    Optional<ForgotPasswordTokenEntity> findByTokenHash(String tokenHash);
}
