package com.devchaves.Pork_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.devchaves.Pork_backend.entity.UserEntity;
import com.devchaves.Pork_backend.entity.VerificationTokenEntity;

public interface TokenRepository extends JpaRepository<VerificationTokenEntity, Long>{

    VerificationTokenEntity findByToken(String token);

    @Query( value = "SELECT * FROM tb_verification_tokens WHERE user_id = :user_id", nativeQuery = true)
    UserEntity findByUserId(Long userId);

}
