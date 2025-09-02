package com.devchaves.Pork_backend.services;

import com.devchaves.Pork_backend.DTO.MetasRequestDTO;
import com.devchaves.Pork_backend.DTO.MetasResponseDTO;
import com.devchaves.Pork_backend.entity.MetasEntity;
import com.devchaves.Pork_backend.entity.UserEntity;
import com.devchaves.Pork_backend.repository.MetasRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MetasService {

    private final MetasRepository metasRepository;
    private static final Logger logger = LoggerFactory.getLogger(MetasService.class);

    public MetasService(MetasRepository metasRepository){
        this.metasRepository = metasRepository;
    }

    @Transactional
    public List<MetasResponseDTO> cadastrarMetas(List<MetasRequestDTO> dtos, UserDetails userDetails){
        logger.info("Cadastrando {} novas metas para o usuário: {}", dtos.size(), userDetails.getUsername());
        UserEntity user = (UserEntity) userDetails;

        if(dtos == null || dtos.isEmpty()){
            logger.warn("Tentativa de cadastrar lista de metas nula ou vazia para o usuário: {}", userDetails.getUsername());
            throw new IllegalArgumentException("Metas não podem conter valores vazios");
        }

        List<MetasEntity> metas = new ArrayList<>();
        for(MetasRequestDTO dto : dtos){
            MetasEntity meta = new MetasEntity();
            meta.setUser(user);
            meta.setMeta(dto.meta());
            meta.setValor(dto.valor());
            meta.setData(dto.data());
            metas.add(meta);
        }

        metasRepository.saveAll(metas);
        logger.info("Metas cadastradas com sucesso para o usuário: {}", userDetails.getUsername());
        return metas.stream().map(m -> new MetasResponseDTO(m.getId(), m.getMeta(), m.getValor(), m.getData())).toList();
    }

    @Transactional
    public MetasResponseDTO alterarMeta(Long id, MetasRequestDTO dto, UserDetails userDetails){
        logger.info("Alterando meta com ID {} para o usuário: {}", id, userDetails.getUsername());
        UserEntity user = (UserEntity) userDetails;

        MetasEntity meta = metasRepository.findById(id).orElseThrow(() -> {
            logger.warn("Meta com ID {} não encontrada para alteração pelo usuário: {}", id, userDetails.getUsername());
            return new IllegalArgumentException("Meta não encontrada");
        });

        if(dto == null){
            logger.warn("Tentativa de alterar meta com dados nulos para o usuário: {}", userDetails.getUsername());
            throw new IllegalArgumentException("Metas não podem conter valores vazios");
        }

        meta.setMeta(dto.meta());
        meta.setValor(dto.valor());
        meta.setData(dto.data());
        meta.setUser(user);

        metasRepository.save(meta);
        logger.info("Meta com ID {} alterada com sucesso para o usuário: {}", id, userDetails.getUsername());
        return new MetasResponseDTO(meta.getId(), meta.getMeta(), meta.getValor(), meta.getData());
    }

    @Transactional
    public void apagarMeta(Long id, UserDetails userDetails){
        logger.info("Apagando meta com ID {} para o usuário: {}", id, userDetails.getUsername());
        UserEntity user = (UserEntity) userDetails;

        if(id == null){
            logger.warn("Tentativa de apagar meta com ID nulo para o usuário: {}", userDetails.getUsername());
            throw new IllegalArgumentException("ID de meta inválido");
        }

        MetasEntity metaParaApagar = metasRepository.findById(id)
                .filter(m -> m.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> {
                    logger.warn("Meta com ID {} não encontrada ou não pertence ao usuário: {}", id, userDetails.getUsername());
                    return new UsernameNotFoundException("Meta não encontrada");
                });

        metasRepository.delete(metaParaApagar);
        logger.info("Meta com ID {} apagada com sucesso para o usuário: {}", id, userDetails.getUsername());
    }

    public List<MetasResponseDTO> consultarMetas(UserDetails userDetails){
        logger.info("Consultando todas as metas para o usuário: {}", userDetails.getUsername());
        UserEntity user = (UserEntity) userDetails;
        Long userId = user.getId();
        List<MetasEntity> metas = metasRepository.findByUserId(userId);
        logger.info("Encontradas {} metas para o usuário: {}", metas.size(), userDetails.getUsername());
        return metas.stream().map(n -> new MetasResponseDTO(
                n.getId(),
                n.getMeta(),
                n.getValor(),
                n.getData()
        )).toList();
    }

    public Page<MetasResponseDTO> consultarMetasPaginadas(Pageable pageable, UserDetails userDetails){
        logger.info("Consultando metas paginadas (página: {}, tamanho: {}) para o usuário: {}", pageable.getPageNumber(), pageable.getPageSize(), userDetails.getUsername());
        UserEntity user = (UserEntity) userDetails;
        Page<MetasEntity> metas = metasRepository.findByUserId(user.getId(), pageable);
        logger.info("Consulta paginada de metas retornou {} elementos.", metas.getNumberOfElements());
        return metas.map(
                m -> new MetasResponseDTO(
                        m.getId(),
                        m.getMeta(),
                        m.getValor(),
                        m.getData()
                )
        );
    }
}
