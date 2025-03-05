package com.ricardosantana.spring.usermanager.models;

import java.time.LocalDateTime;

import org.hibernate.validator.constraints.Length;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ricardosantana.spring.usermanager.dtos.LoginRequest;
import com.ricardosantana.spring.usermanager.enums.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @NotNull
    private LocalDateTime dataHoraCadastro;

    @NotBlank
    private String usuarioCadastrado;

    @NotBlank
    @Size(min = 3 , max = 35)
    @Length(min = 3, max = 35)
    @Column(length = 35)
    private String nome;

    @NotBlank
    @Email
    @Size(min = 5,max = 50)
    @Column(length = 50)
    private String email;

    @NotBlank
    @Size(min = 6)
    @Length(min = 6)
    private String senha;

    @NotBlank
    @Size(min = 11, max = 11)
    @Length(min = 11, max = 11)
    @Column(length = 11)
    private String telefone;

    @NotNull
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