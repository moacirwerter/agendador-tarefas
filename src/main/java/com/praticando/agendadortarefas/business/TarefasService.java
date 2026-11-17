package com.praticando.agendadortarefas.business;

import com.praticando.agendadortarefas.business.dto.TarefasDTO;
import com.praticando.agendadortarefas.business.mapper.TarefaUpdateConverter;
import com.praticando.agendadortarefas.business.mapper.TarefasConverter;
import com.praticando.agendadortarefas.infrastructure.entity.TarefasEntity;
import com.praticando.agendadortarefas.infrastructure.enums.StatusNotificacaoEnum;
import com.praticando.agendadortarefas.infrastructure.exceptions.ResourceNotFoundException;
import com.praticando.agendadortarefas.infrastructure.repository.TarefasRepository;
import com.praticando.agendadortarefas.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TarefasService {

    private final TarefasRepository tarefaRepository;
    private final TarefasConverter tarefaConverter;
    private final JwtUtil jwtUtil;
    private final TarefaUpdateConverter tarefaUpdateConverter;

    // Método auxiliar privado para limpar o token com segurança
    private String limparToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7).trim();
        }
        return token != null ? token.trim() : "";
    }

    public TarefasDTO gravarTarefa(String token, TarefasDTO dto) {
        String email = jwtUtil.extrairEmailToken(limparToken(token));

        dto.setDataCriacao(LocalDateTime.now());
        dto.setStatusNotificacaoEnum(StatusNotificacaoEnum.PENDENTE);
        dto.setEmailUsuario(email);

        TarefasEntity entity = tarefaConverter.paraTarefaEntity(dto);
        TarefasEntity entidadeSalva = tarefaRepository.save(entity);

        return tarefaConverter.paraTarefaDTO(entidadeSalva);
    }

    public List<TarefasDTO> buscaTarefasAgendadasPorPeriodo(LocalDateTime dataInicial, LocalDateTime dataFinal) {
        return tarefaConverter.paraListaTarefaDTO(
                tarefaRepository.findByDataEventoBetween(dataInicial, dataFinal)
        );
    }

    public List<TarefasDTO> buscaTarefasPorEmail(String token) {
        String email = jwtUtil.extrairEmailToken(limparToken(token));
        List<TarefasEntity> listaTarefas = tarefaRepository.findByEmailUsuario(email);

        return tarefaConverter.paraListaTarefaDTO(listaTarefas);
    }

    public void deletaTarefaPorID(String id) {
        tarefaRepository.deleteById(id);
    }

    public TarefasDTO alteraStatus(StatusNotificacaoEnum status, String id) {
        try {
            TarefasEntity entity = tarefaRepository.findById(id).orElseThrow(()
                    -> new ResourceNotFoundException("Tarefa não encontrada com o id: " + id));

            entity.setStatusNotificacaoEnum(status);
            TarefasEntity entidadeSalva = tarefaRepository.save(entity);

            return tarefaConverter.paraTarefaDTO(entidadeSalva);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Erro ao alterar status da tarefa: " + id, e);
        }
    }

    public TarefasDTO updateTarefas(TarefasDTO dto, String id) {
        try {
            TarefasEntity entity = tarefaRepository.findById(id).orElseThrow(()
                    -> new ResourceNotFoundException("Tarefa não encontrada com o id: " + id));

            tarefaUpdateConverter.updateTarefas(dto, entity);

            return tarefaConverter.paraTarefaDTO(tarefaRepository.save(entity));

        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Erro ao alterar status da tarefa: " + id, e);
        }
    }
}
