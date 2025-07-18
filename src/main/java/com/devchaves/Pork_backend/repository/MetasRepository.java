package com.devchaves.Pork_backend.repository;

import com.devchaves.Pork_backend.entity.MetasEntity;
import com.devchaves.Pork_backend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MetasRepository extends JpaRepository<MetasEntity, Long> {

    @Query(value = "SELECT * FROM tb_metas WHERE user_id = :user_id", nativeQuery = true)
    public UserEntity findByUserId(Long id);

}
