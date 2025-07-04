package com.devchaves.Pork_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devchaves.Pork_backend.entity.VerificationTokenEntity;

public interface TokenRepository extends JpaRepository<VerificationTokenEntity, Long>{

    
    VerificationTokenEntity findByToken(String token);

}
