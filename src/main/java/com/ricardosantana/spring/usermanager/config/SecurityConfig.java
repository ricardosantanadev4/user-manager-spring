package com.ricardosantana.spring.usermanager.config;

import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /*
     * Referencia o caminho para encontrar a chave pública e privada
     * Essas chaves são utilizadas para encoder e decoder das informações
     * 
     * @Value("${}") esta referenciando o aplication.properties
     */
    @Value("${jwt.public.key}")
    private RSAPublicKey publicKey;

    // // Ativar quuando for testar localmente
    // @Value("${jwt.private.key}")
    // private RSAPrivateKey privateKey;

    // Ativar quando for para o servidor de hospedagem
    @Value("${jwt.private.key}")
    private String privateKeyPem;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Habilita CORS
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/token/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/usuarios/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/usuarios/criar").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/usuarios/**").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/usuarios/**").hasAnyRole("ADMIN")
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        )
                .csrf(csrf -> csrf.disable())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(
                        jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())))
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

    /*
     * Valida e decodifica tokens JWT recebidos em requisições HTTP.
     * Usa a chave pública (publicKey) para validar a assinatura do token JWT.
     * Retorna um objeto do tipo JwtDecoder, que pode ser usado para extrair os
     * dados do token.
     * Permitindo acessar:
     * jwt.getHeaders(); // Retorna o cabeçalho do token JWT
     * jwt.getClaims(); // Retorna o payload (dados do token)
     */
    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(publicKey).build();
    }

    // /*
    //  * Ativar quando for testar localmente
    //  * Cria e retorna um JwtEncoder, que é responsável por gerar tokens JWT
    //  * assinados usando uma chave RSA.
    //  * Detalhes:
    //  * Cria uma chave RSA usando a chave pública e privada.
    //  * this.publicKey: Chave pública (usada para validar tokens assinados).
    //  * this.privateKey: Chave privada (usada para assinar tokens JWT).
    //  * Retorna um encoder JWT que usa essa chave para assinar tokens.
    //  */
    // @Bean
    // public JwtEncoder jwtEncoder() {
    //     JWK jwk = new RSAKey.Builder(this.publicKey).privateKey(privateKey).build();
    //     var jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
    //     return new NimbusJwtEncoder(jwks);
    // }

    // Ativar quando for para o servidor de hospedagem
    @Bean
    public JwtEncoder jwtEncoder() throws Exception {
    // Converte a chave privada da variável de ambiente para RSAPrivateKey
    RSAPrivateKey privateKey = getPrivateKeyFromPEM(privateKeyPem);

    JWK jwk = new RSAKey.Builder(publicKey).privateKey(privateKey).build();
    var jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
    return new NimbusJwtEncoder(jwks);
    }

    // Ativar quando for para o servidor de hospedagem
    private RSAPrivateKey getPrivateKeyFromPEM(String privateKeyPem) throws
    Exception {
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

    /*
     * Retorna uma nova instância de BCryptPasswordEncoder, que estará disponível
     * para codificar ou comparar senhas.
     * Ex de uso:
     * Criptografar a senha do usuario quando ele é salvo no banco de dados
     * Verificar se a senha fornencida pelo usuário durante o login é valida
     * comparando a senha fornecida com armazenada no banco dedados por meio de hash
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Converte claims extras, como "role", para authorities do Spring Security
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();

        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
            JwtGrantedAuthoritiesConverter defaultConverter = new JwtGrantedAuthoritiesConverter();
            Collection<GrantedAuthority> authorities = defaultConverter.convert(jwt);

            // Extraindo a claim "role" do token JWT
            String role = jwt.getClaim("role");
            if (role != null) {
                authorities = Stream.concat(
                        authorities.stream(),
                        Stream.of(new SimpleGrantedAuthority(role)) // Adiciona a role do token
                ).collect(Collectors.toList());
            }

            return authorities;
        });

        return jwtAuthenticationConverter;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(
                "http://localhost:4200",
                "https://user-manager-angular.vercel.app",
                "https://user-manager-spring.onrender.com"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*")); // Permite todos os headers na requisição
        config.setExposedHeaders(List.of(HttpHeaders.AUTHORIZATION)); // 🔥 Expondo o header Authorization
        config.setAllowCredentials(true); // 🔥 Importante para autenticação

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}

