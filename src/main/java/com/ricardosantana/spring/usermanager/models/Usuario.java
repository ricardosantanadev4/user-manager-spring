package com.ricardosantana.spring.usermanager.models;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.ricardosantana.spring.usermanager.dtos.LoginRequest;
import com.ricardosantana.spring.usermanager.enums.Role;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime dataHoraCadastro;
    private String usuarioCadastrado;
    private String nome;
    private String email;
    private String senha;
    private String telefone;

    @Enumerated(EnumType.STRING)
    private Role role;

    /*
     * Compara a senha fornecida pelo usuário (loginRequest.senha()), que ainda
     * está em texto plano.
     * A senha armazenada no banco de dados (this.senha), que foi codificada durante
     * a criação do usuario.
     * Aplica o mesmo algoritmo de codificação (hashing) na senha fornecida pelo
     * usuário, e então verifica se ela corresponde ao hash da senha armazenada
     * (this.senha).
     */
    public boolean isLoginCorrect(LoginRequest loginRequest, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(loginRequest.senha(), this.senha);
    }
}