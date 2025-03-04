package com.ricardosantana.spring.usermanager.profiles.dev.config;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;

@Configuration
@Profile("dev")
public class SecurityDevConfig {

    /*
     * Referencia o caminho para encontrar a chave pública e privada
     * Essas chaves são utilizadas para encoder e decoder das informações
     * 
     * @Value("${}") esta referenciando o aplication.properties
     */
    @Value("${jwt.public.key}")
    private RSAPublicKey publicKey;

    // Configuração de ambiente de desenvolvimento
    @Value("${jwt.private.key}")
    private RSAPrivateKey privateKey;

    /*
     * Configuração de ambiente de desenvolvimento.
     * Cria e retorna um JwtEncoder, que é responsável por gerar tokens JWT
     * assinados usando uma chave RSA.
     * Detalhes:
     * Cria uma chave RSA usando a chave pública e privada.
     * this.publicKey: Chave pública (usada para validar tokens assinados).
     * this.privateKey: Chave privada (usada para assinar tokens JWT).
     * Retorna um encoder JWT que usa essa chave para assinar tokens.
     */
    @Bean
    public JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(this.publicKey).privateKey(privateKey).build();
        var jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

}