package com.ricardosantana.spring.usermanager.dtos;

import java.time.LocalDateTime;

import com.ricardosantana.spring.usermanager.enums.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UsuarioDTO(
        Long id, 

        LocalDateTime dataHoraCadastro, 

        @NotBlank
        @Size(min = 3 , max = 35)
        String usuarioCadastrado,

        @NotBlank
        @Size(min = 3 , max = 35)
        String nome, 

        @NotBlank
        @Email
        String email, 

        @NotBlank
        @Size(min = 6 , max = 20)
        String senha, 

        @NotBlank
        @Size(min = 11, max = 11)
        String telefone, 

        @NotNull
        Role role) {
}