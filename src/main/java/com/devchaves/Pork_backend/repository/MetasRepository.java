package com.devchaves.Pork_backend.repository;

import com.devchaves.Pork_backend.entity.MetasEntity;
import com.devchaves.Pork_backend.entity.UserEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MetasRepository extends JpaRepository<MetasEntity, Long> {

    List<MetasEntity> findByUserId(Long userId);

    @Modifying
@Query(value = "DELETE FROM MetasEntity m WHERE m.id = :metaId AND m.user.id = :userId")
     int deletarMeta(@Param("metaId") Long metaId, @Param("userId") Long userId);

}
