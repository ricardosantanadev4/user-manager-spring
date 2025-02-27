package com.ricardosantana.spring.usermanager.controllers;

import java.time.Instant;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ricardosantana.spring.usermanager.dtos.LoginRequest;
import com.ricardosantana.spring.usermanager.dtos.LoginResponse;
import com.ricardosantana.spring.usermanager.models.Usuario;
import com.ricardosantana.spring.usermanager.repositorys.UsuarioRepository;

@RestController
@RequestMapping("/token")
public class TokenController {

    private final JwtEncoder jwtEncoder;
    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public TokenController(JwtEncoder jwtEncoder, UsuarioRepository usuarioRepository,
            BCryptPasswordEncoder bcryptPasswordEncoder) {
        this.jwtEncoder = jwtEncoder;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = bcryptPasswordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(loginRequest.email());

        /*
         * se user estiver vazio retorna uma exececao, caso contraro verifica se a senha
         * passada pelo usuario e igual a senha do banco de dados
         */
        if (usuario.isEmpty() || !usuario.get().isLoginCorrect(loginRequest, passwordEncoder)) {
            throw new BadCredentialsException("user or password is invalid!");
        }

        var now = Instant.now();
        var expiresIn = 300L;

        // gera o token jwt configurando os atributos do json do token jwt e retorna na
        // requizicao
        var claims = JwtClaimsSet.builder()
                .issuer("mybackend")
                .subject(usuario.get().getId().toString())
                .claim("role", "ROLE_" + usuario.get().getRole())
                .claim("nome", usuario.get().getNome())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn)).build();

        // criptografa os json do jwt com a chave privada e gera o valor do tokem com os
        // dados criptografados
        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        // return ResponseEntity.ok(new LoginResponse(jwtValue, expiresIn));

        return ResponseEntity.ok()
    .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtValue)
    .build();
    }
}
