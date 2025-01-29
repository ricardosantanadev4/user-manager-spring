package com.ricardosantana.spring.usermanager.services;

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
        String[] nomes = { "User-01", "User-02", "User-03", "User-04" };

        int numeroDeTestes = 30;
        for (int i = 0; i < numeroDeTestes; i++) {
            Usuario usuario = new Usuario(
                    null,
                    nomes[i % nomes.length],
                    "email" + (i + 1) + "@gmail.com",
                    "81999999999");
            this.usuarioRepository.save(usuario);
        }
        return true;
    }

}
