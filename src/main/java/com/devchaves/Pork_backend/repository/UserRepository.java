package com.devchaves.Pork_backend.repository;

import com.devchaves.Pork_backend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query(value = "SELECT * FROM tb_usuarios WHERE email = :email", nativeQuery = true)
    Optional<UserEntity> findByEmail(@Param("email")String email);

    @Query(value = "SELECT COUNT(*) > 0 FROM tb_usuarios WHERE email = :email", nativeQuery = true)
    boolean existsByEmail(@Param("email")String email);

    @Query(value = "SELECT * FROM tb_usuarios WHERE token = :token", nativeQuery = true)
    Optional<UserEntity> findByToken(@Param("token") String token);

    @Modifying
    @Query(value = "UPDATE tb_usuarios SET receita = :receita WHERE id = :userId", nativeQuery = true)
    void updateReceita(@Param("userId") Long userId, @Param("receita")BigDecimal receita);

}
