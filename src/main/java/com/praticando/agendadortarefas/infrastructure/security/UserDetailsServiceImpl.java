package com.praticando.agendadortarefas.infrastructure.security;

import com.praticando.agendadortarefas.business.dto.UsuarioDTO;
import com.praticando.agendadortarefas.infrastructure.client.usuarioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserDetailsServiceImpl {

    @Autowired
    private usuarioClient client;

    public UserDetails carregaDadosUsuario(String email, String token) {
        try {
            // Envia os dados envelopados em um Map para o Feign Client
            UsuarioDTO usuarioDTO = client.buscarPorEmail(Map.of("email", email), token);

            return User
                    .withUsername(usuarioDTO.getEmail())
                    .password(usuarioDTO.getSenha() != null ? usuarioDTO.getSenha() : "")
                    .roles("USER") // Define a permissão padrão
                    .build();

        } catch (feign.FeignException.Conflict e) {
            // INTERCEPTA O ERRO 409: Se o e-mail conflitar ou já existir,
            // avisa o Spring Security lançando a exceção correta de autenticação
            throw new UsernameNotFoundException("Usuário em conflito ou já cadastrado no sistema: " + email, e);
        }
    }
}
