package com.ricardosantana.spring.usermanager.services;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.ricardosantana.spring.usermanager.models.Usuario;
import com.ricardosantana.spring.usermanager.repositories.UsuarioRepository;

@Service
public class DbService {

    private final UsuarioRepository usuarioRepository;

    public DbService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public boolean instanciaDB() {
        int numeroDeTestes = 20;
        for (int i = 0; i < numeroDeTestes; i++) {
            Usuario usuario = new Usuario(
                    null,
                    LocalDateTime.of(2025, 01, 1 + i, 00, 00, i++),
                    "Admin",
                    "User-" + (i + 1),
                    "email" + (i + 1) + "@gmail.com",
                    "81999999999");
            this.usuarioRepository.save(usuario);
        }
        return true;
    }

}
