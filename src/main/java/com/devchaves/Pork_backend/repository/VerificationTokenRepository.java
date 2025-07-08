package com.devchaves.Pork_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.devchaves.Pork_backend.entity.VerificationTokenEntity;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationTokenEntity, Long> {

    @Query(value = "SELECT * FROM tb_verification_tokens WHERE token = :token", nativeQuery = true)
    Optional<VerificationTokenEntity> findToken(@Param("token")String token);

}
