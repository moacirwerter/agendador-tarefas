package com.praticando.agendadortarefas.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import com.praticando.agendadortarefas.business.dto.UsuarioDTO;
import java.util.Map;

@FeignClient(name = "usuario-client", url = "http://localhost:8080") // ou a configuração que já existir aí
public interface usuarioClient {

    @PostMapping(value = "/usuario", consumes = "application/json")
    UsuarioDTO buscarPorEmail(@RequestBody Map<String, String> body, @RequestHeader("Authorization") String token);
}
