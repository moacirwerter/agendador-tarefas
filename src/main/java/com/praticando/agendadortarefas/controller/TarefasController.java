package com.praticando.agendadortarefas.controller;

import com.praticando.agendadortarefas.business.TarefasService;
import com.praticando.agendadortarefas.business.dto.TarefasDTO;
import com.praticando.agendadortarefas.infrastructure.enums.StatusNotificacaoEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/tarefas")
@RequiredArgsConstructor
public class TarefasController {

    private final TarefasService tarefasService;

    @PostMapping
    public ResponseEntity<TarefasDTO> gravarTarefas(@RequestBody TarefasDTO dto,
                                                    @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(tarefasService.gravarTarefa(token, dto));
    }

    @GetMapping("/eventos")
    public ResponseEntity<List<TarefasDTO>> buscaListaDeTarefasPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicial,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFinal) {

        return ResponseEntity.ok(tarefasService.buscaTarefasAgendadasPorPeriodo(dataInicial, dataFinal));
    }

    @GetMapping("/email")
    public ResponseEntity<List<TarefasDTO>> buscaTarefasPorEmail(@RequestHeader("Authorization") String token) {
        List<TarefasDTO> tarefas = tarefasService.buscaTarefasPorEmail(token);
        return ResponseEntity.ok(tarefas);
    }

    @DeleteMapping
    public ResponseEntity<Void> deletaTarefaPorId(@RequestParam("id") String id) {
        tarefasService.deletaTarefaPorID(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/altera-status")
    public ResponseEntity<TarefasDTO> alteraStatus(@RequestParam("status") StatusNotificacaoEnum status,
                                                   @RequestParam("id") String id) {
        TarefasDTO dto = tarefasService.alteraStatus(status, id);
        return ResponseEntity.ok(dto);
    }

    @PatchMapping("/alterar-status-notificacao") // CORRIGIDO: Adicionado complemento de rota para evitar colisão
    public ResponseEntity<TarefasDTO> alterarStatusNotificacao(
            @RequestParam("status") StatusNotificacaoEnum status,
            @RequestParam("id") String id) { // CORRIGIDO: Parâmetro agora dentro do escopo correto do método

        return ResponseEntity.ok(tarefasService.alteraStatus(status, id));
    }

    @PutMapping("/update") // CORRIGIDO: Adicionado mapeamento específico para a edição
    public ResponseEntity<TarefasDTO> updateTarefas(@RequestBody TarefasDTO dto,
                                                    @RequestParam("id") String id) {
        // CORRIGIDO: Adicionado o ponto e vírgula ";" que faltava no final
        return ResponseEntity.ok(tarefasService.updateTarefas(dto, id));
    }
}
