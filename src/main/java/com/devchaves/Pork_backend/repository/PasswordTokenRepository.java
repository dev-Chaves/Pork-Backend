package com.devchaves.Pork_backend.repository;

import com.devchaves.Pork_backend.entity.PasswordTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordTokenRepository extends JpaRepository<PasswordTokenEntity, Long> {
    Optional<PasswordTokenEntity> findByToken(String token);
}
