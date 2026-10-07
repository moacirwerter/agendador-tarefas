package com.praticando.agendadortarefas.business.mapper;


import com.praticando.agendadortarefas.business.dto.TarefasDTO;
import com.praticando.agendadortarefas.infrastructure.entity.TarefasEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TarefasConverter {
    TarefasEntity paraTarefaEntity(TarefasDTO dto);
    TarefasDTO paraTarefaDTO(TarefasEntity entity);
}
