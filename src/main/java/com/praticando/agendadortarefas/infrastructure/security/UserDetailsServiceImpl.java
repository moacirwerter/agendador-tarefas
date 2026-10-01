package com.praticando.agendadortarefas.infrastructure.security;

import com.praticando.agendadortarefas.business.dto.UsuarioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.praticando.agendadortarefas.infrastructure.client.usuarioClient;

import java.util.ArrayList;

@Service
public class UserDetailsServiceImpl {

    @Autowired
    private usuarioClient client;


    public UserDetails carregaDadosUsuario(String email, String token) {

        UsuarioDTO usuarioDTO = client.buscaUsuarioPorEmail(email, token);
        return User

                .withUsername(usuarioDTO.getEmail())
                .password(usuarioDTO.getSenha()) // Senha fictícia criptografada

                .build();
    }


}
