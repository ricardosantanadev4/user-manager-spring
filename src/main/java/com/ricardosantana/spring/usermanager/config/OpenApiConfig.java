package com.ricardosantana.spring.usermanager.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

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

                        url = "https://user-manager-spring-production.up.railway.app/api",

                        description = "Servidor de Produção")
        })
public class OpenApiConfig {

}
