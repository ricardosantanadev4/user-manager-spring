package com.ricardosantana.spring.usermanager.dtos;

import java.time.LocalDateTime;

public record UsuarioDTO(Long id, LocalDateTime dataHoraCadastro, String usuarioCadastrado,
                String nome, String email, String telefone) {

}