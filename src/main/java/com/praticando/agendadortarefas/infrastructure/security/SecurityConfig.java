
package com.praticando.agendadortarefas.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;

    // Injeta o filtro responsável pela autenticação JWT
    public SecurityConfig(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    ) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http
                // API REST utilizando JWT
                .csrf(AbstractHttpConfigurer::disable)

                // Não utiliza sessões HTTP para autenticação
                .sessionManagement(session -> session
                        .sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                // Configuração de autorização
                .authorizeHttpRequests(auth -> auth

                        // Documentação Swagger
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        // Cadastro de tarefas: exige JWT
                        .requestMatchers(
                                HttpMethod.POST,
                                "/tarefas"
                        ).authenticated()

                        // Consulta de tarefas do usuário: exige JWT
                        .requestMatchers(
                                HttpMethod.GET,
                                "/tarefas/email"
                        ).authenticated()

                        // Consulta de eventos: acesso público
                        .requestMatchers(
                                HttpMethod.GET,
                                "/tarefas/eventos"
                        ).permitAll()

                        // Exclusão de tarefas: exige JWT
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/tarefas"
                        ).authenticated()

                        // Atualização de tarefas: exige JWT
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/tarefas/**"
                        ).authenticated()

                        // Demais requisições exigem autenticação
                        .anyRequest().authenticated()
                )

                // Executa o filtro JWT antes do filtro padrão
                .addFilterBefore(
                        jwtRequestFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}
