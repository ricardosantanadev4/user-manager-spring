package com.ricardosantana.spring.usermanager.services;

import java.time.LocalDateTime;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ricardosantana.spring.usermanager.enums.Role;
import com.ricardosantana.spring.usermanager.models.Usuario;
import com.ricardosantana.spring.usermanager.repositorys.UsuarioRepository;

@Service
public class DbService {

    private final UsuarioRepository usuarioRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    public DbService(UsuarioRepository usuarioRepository, BCryptPasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean instanciaDB() {
        int numeroDeTestes = 40;
        for (int i = 0; i < numeroDeTestes; i++) {
            Usuario usuario = new Usuario(
                    null,
                    LocalDateTime.of(2025, 01, 1 + i, 00, 00, i++),
                    "Admin",
                    "User-" + (i + 1),
                    "email" + (i + 1) + "@gmail.com",
                    passwordEncoder.encode("Dev#123"),
                    "81999999999",
                    Role.USER);
            this.usuarioRepository.save(usuario);
        }
        return true;
    }

}
