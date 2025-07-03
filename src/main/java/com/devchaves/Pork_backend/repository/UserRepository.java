package com.devchaves.Pork_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.devchaves.Pork_backend.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query(value = "SELECT * FROM tb_usuarios WHERE email = :email", nativeQuery = true)
    Optional<UserEntity> findByEmail(@Param("email")String email);

    @Query(value = "SELECT COUNT(*) > 0 FROM tb_usuarios WHERE email = :email", nativeQuery = true)
    boolean existsByEmail(@Param("email")String email);


}
