package com.devchaves.Pork_backend.services;

import com.devchaves.Pork_backend.DTO.MetasRequestDTO;
import com.devchaves.Pork_backend.DTO.MetasResponseDTO;
import com.devchaves.Pork_backend.entity.MetasEntity;
import com.devchaves.Pork_backend.entity.UserEntity;
import com.devchaves.Pork_backend.repository.MetasRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MetasService {

    private final MetasRepository metasRepository;

    private final UtilServices utilServices;

    public MetasService(MetasRepository metasRepository, UtilServices utilServices){
        this.metasRepository = metasRepository;
        this.utilServices = utilServices;
    }

    public List<MetasResponseDTO> cadastrarMetas(List<MetasRequestDTO> dtos){

        UserEntity user = utilServices.getCurrentUser();

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

        return metas.stream().map(m -> new MetasResponseDTO(m.getMeta(), m.getValor(), m.getData())).toList();

    }

    public MetasResponseDTO alterarMeta(Long id, MetasRequestDTO dto){

        MetasEntity meta = metasRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Meta não encontrada"));

        if(dto == null){
            throw new IllegalArgumentException("Metas não podem conter valores vazios");
        }

        meta.setMeta(dto.meta());
        meta.setValor(dto.valor());
        meta.setData(dto.data());

        metasRepository.save(meta);

        return new MetasResponseDTO(meta.getMeta(), meta.getValor(), meta.getData());

    }

    public void apagarMeta(Long id){

        UserEntity user = utilServices.getCurrentUser();

        if(id == null){
            throw new IllegalArgumentException("ID de meta inválido");
        }

        metasRepository.delete(metasRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Meta não encontrada")));

    }

    public List<MetasResponseDTO> consultarMetas(){

        UserEntity user = utilServices.getCurrentUser();

        List<MetasEntity> metas = metasRepository.findByUserId(user.getId());

        return metas.stream().map(n -> new MetasResponseDTO(
                n.getMeta(),
                n.getValor(),
                n.getData()
        )).toList();

    }

}
