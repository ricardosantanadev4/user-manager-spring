package com.ricardosantana.spring.usermanager.config;

import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@OpenAPIDefinition(

        info = @Info(

                title = "User Manager API",

                version = "v1.0",

                description = "API para gerenciamento de usuários, incluindo criação, "
                        + "listagem, atualização e exclusão.",

                contact = @Contact(

                        name = "Ricardo Santana",

                        email = "ricardosantanadev4@gmail.com",

                        url = "https://www.linkedin.com/in/ricardo-silva-de-santana-08b91829b/"),

                license = @License(

                        name = "MIT License",

                        url = "https://opensource.org/licenses/MIT")),

        servers = {
                @Server(

                        url = "http://localhost:8080/api",

                        description = "Servidor Local"),
                @Server(

                        url = "https://user-manager-spring.onrender.com/api",

                        description = "Servidor de Produção")
        })
public class OpenApiConfig {
@Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList("BearerAuth")) // 🔥 Exige autenticação no Swagger
                .components(new Components().addSecuritySchemes("BearerAuth",
                        new SecurityScheme()
                                .name("BearerAuth")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT"))); // 🔥 Define autenticação via JWT
    }
}
