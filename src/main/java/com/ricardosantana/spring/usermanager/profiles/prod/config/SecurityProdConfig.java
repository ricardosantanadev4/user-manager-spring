package com.ricardosantana.spring.usermanager.profiles.prod.config;

import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

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
@Profile("prod")
public class SecurityProdConfig {

 /*
     * Referencia o caminho para encontrar a chave pública e privada
     * Essas chaves são utilizadas para encoder e decoder das informações
     * 
     * @Value("${}") esta referenciando o aplication.properties
     */
    @Value("${jwt.public.key}")
    private RSAPublicKey publicKey;
    
// Configuração de ambiente de produção
    @Value("${jwt.private.key}")
    private String privateKeyPem;

    // Configuração de ambiente de produção.
    @Bean
    public JwtEncoder jwtEncoder() throws Exception {
        // Converte a chave privada da variável de ambiente para RSAPrivateKey
        RSAPrivateKey privateKey = getPrivateKeyFromPEM(privateKeyPem);

        JWK jwk = new RSAKey.Builder(publicKey).privateKey(privateKey).build();
        var jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    // Configuração de ambiente de produção.
    private RSAPrivateKey getPrivateKeyFromPEM(String privateKeyPem) throws Exception {
        // Remove cabeçalhos e rodapés do formato PEM
        String privateKeyPEM = privateKeyPem
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", ""); // Remove espaços e quebras de linha

        byte[] keyBytes = Base64.getDecoder().decode(privateKeyPEM);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
    }
}
