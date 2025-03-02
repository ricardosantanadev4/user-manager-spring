package com.ricardosantana.spring.usermanager.config;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.ricardosantana.spring.usermanager.enums.Role;
import com.ricardosantana.spring.usermanager.models.Usuario;
import com.ricardosantana.spring.usermanager.repositorys.UsuarioRepository;

// classe gerada para criar um usuario no banco de dados com o password criptografado a partir da chave privada

@Configuration
public class AdminUserConfig implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AdminUserConfig(com.ricardosantana.spring.usermanager.repositorys.UsuarioRepository usuarioRepository,
            BCryptPasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        Optional<Usuario> userAdmin = usuarioRepository.findByEmail("admin");

        userAdmin.ifPresentOrElse(
                user -> {
                    System.out.println("admin ja existe");
                },
                () -> {
                    Usuario usuarioAdmin = new Usuario();
                    usuarioAdmin.setDataHoraCadastro(LocalDateTime.now());
                    usuarioAdmin.setUsuarioCadastrado("Desenvolvimento");
                    usuarioAdmin.setNome("Amdmin");
                    usuarioAdmin.setEmail("admin@domain.com");
                    usuarioAdmin.setSenha(passwordEncoder.encode("Admin#2025"));
                    usuarioAdmin.setTelefone("81777777777");
                    usuarioAdmin.setRole(Role.ADMIN);
                    usuarioRepository.save(usuarioAdmin);

                    Usuario usuario = new Usuario();
                    usuario.setDataHoraCadastro(LocalDateTime.now());
                    usuario.setUsuarioCadastrado("Desenvolvimento");
                    usuario.setNome("User");
                    usuario.setEmail("user@domain.com");
                    usuario.setSenha(passwordEncoder.encode("User#2025"));
                    usuario.setTelefone("81777777777");
                    usuario.setRole(Role.USER);
                    usuarioRepository.save(usuario);
                });
    }
}
