package com.ricardosantana.spring.usermanager.dtos;

import java.time.LocalDateTime;

public record UsuarioDTO(Long id, LocalDateTime dataHoraCadastro, String usuarioCriador,
        String nome, String email, String telefone) {

}