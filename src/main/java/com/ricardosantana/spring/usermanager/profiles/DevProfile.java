package com.ricardosantana.spring.usermanager.profiles;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.ricardosantana.spring.usermanager.services.DbService;

@Configuration
@Profile("dev")
public class DevProfile {

    private final DbService dbService;

    public DevProfile(DbService dbService) {
        this.dbService = dbService;
    }

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddl;

    @Bean
    public boolean instanciaDB() {
        if (ddl.equals("create-drop")) {
            this.dbService.instanciaDB();
        }

        return false;
    }
}
