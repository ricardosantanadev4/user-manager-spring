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
        String[] nomes = { "Ricardo", "Bolsonaro", "Luiz Inácio Lula Da Silva", "Amanda" };

        int numeroDeTestes = 40;
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
