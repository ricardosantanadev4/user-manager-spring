package com.ricardosantana.spring.usermanager.profiles;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.ricardosantana.spring.usermanager.services.DbService;

@Configuration
@Profile("test")
public class TestProfile {

    private final DbService dbService;

    public TestProfile(DbService dbService) {
        this.dbService = dbService;
    }

    @Bean
    public void instaciaDB() {
        this.dbService.instanciaDB();
    }
}
