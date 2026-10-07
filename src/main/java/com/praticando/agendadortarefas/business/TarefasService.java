package com.praticando.agendadortarefas.business;


import com.praticando.agendadortarefas.business.dto.TarefasDTO;
import com.praticando.agendadortarefas.business.mapper.TarefasConverter;
import com.praticando.agendadortarefas.infrastructure.entity.TarefasEntity;
import com.praticando.agendadortarefas.infrastructure.enums.StatusNotificacaoEnum;
import com.praticando.agendadortarefas.infrastructure.repository.TarefasRepository;
import com.praticando.agendadortarefas.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TarefasService {

    private final TarefasRepository tarefaRepository;
    private final TarefasConverter tarefaConverter;
    private final JwtUtil jwtUtil;

    public TarefasDTO gravarTarefa(String token ,TarefasDTO dto) {
        String email= jwtUtil.extrairEmailToken(token.substring(7 ));
        dto.setDataCriacao(LocalDateTime.now());
        dto.setStatusNotificacaoEnum(StatusNotificacaoEnum.PENDENTE);
        dto.setEmailUsuario(email);
        TarefasEntity entity = tarefaConverter.paraTarefaEntity(dto);
        TarefasEntity entidadeSalva = tarefaRepository.save(entity);


        return tarefaConverter.paraTarefaDTO(entidadeSalva);

    }
}
