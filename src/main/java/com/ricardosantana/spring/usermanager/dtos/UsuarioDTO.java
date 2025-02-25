package com.ricardosantana.spring.usermanager.dtos;

import java.time.LocalDateTime;

import com.ricardosantana.spring.usermanager.enums.Role;

public record UsuarioDTO(Long id, LocalDateTime dataHoraCadastro, String usuarioCadastrado,
        String nome, String email, String senha, String telefone, Role role) {

}