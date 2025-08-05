package com.devchaves.Pork_backend.repository;

import com.devchaves.Pork_backend.entity.MetasEntity;
import com.devchaves.Pork_backend.entity.UserEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MetasRepository extends JpaRepository<MetasEntity, Long> {

    List<MetasEntity> findByUserId(Long userId);

}
