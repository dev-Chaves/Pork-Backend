package com.devchaves.Pork_backend.services;

import com.devchaves.Pork_backend.DTO.MetasRequestDTO;
import com.devchaves.Pork_backend.DTO.MetasResponseDTO;
import com.devchaves.Pork_backend.entity.MetasEntity;
import com.devchaves.Pork_backend.entity.UserEntity;
import com.devchaves.Pork_backend.repository.MetasRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MetasService {

    private final MetasRepository metasRepository;

    public MetasService(MetasRepository metasRepository){
        this.metasRepository = metasRepository;
    }

    @Transactional
    public List<MetasResponseDTO> cadastrarMetas(List<MetasRequestDTO> dtos, UserDetails userDetails){

        UserEntity user = (UserEntity) userDetails;

        if(dtos == null){
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

        return metas.stream().map(m -> new MetasResponseDTO(m.getId(), m.getMeta(), m.getValor(), m.getData())).toList();

    }

    @Transactional
    public MetasResponseDTO alterarMeta(Long id, MetasRequestDTO dto, UserDetails userDetails){

        UserEntity user = (UserEntity) userDetails;

        MetasEntity meta = metasRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Meta não encontrada"));

        if(dto == null){
            throw new IllegalArgumentException("Metas não podem conter valores vazios");
        }

        meta.setMeta(dto.meta());
        meta.setValor(dto.valor());
        meta.setData(dto.data());
        meta.setUser(user);

        metasRepository.save(meta);

        return new MetasResponseDTO(meta.getId(), meta.getMeta(), meta.getValor(), meta.getData());

    }

    @Transactional
    public void apagarMeta(Long id, UserDetails userDetails){

        UserEntity user = (UserEntity) userDetails;

        if(id == null){
            throw new IllegalArgumentException("ID de meta inválido");
        }

        MetasEntity metaParaApagar = metasRepository.findById(id).filter(m -> m.getUser().getId().equals(user.getId()))
                        .orElseThrow(() -> new UsernameNotFoundException("Meta não encontrada"));

        metasRepository.delete(metaParaApagar);

    }

    public List<MetasResponseDTO> consultarMetas(UserDetails userDetails){

        UserEntity user = (UserEntity) userDetails;

        Long userId = user.getId();

        List<MetasEntity> metas = metasRepository.findByUserId(userId);

        return metas.stream().map(n -> new MetasResponseDTO(
                n.getId(),
                n.getMeta(),
                n.getValor(),
                n.getData()
        )).toList();

    }

}
