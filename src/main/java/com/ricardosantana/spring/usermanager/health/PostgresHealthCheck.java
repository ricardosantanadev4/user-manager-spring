package com.ricardosantana.spring.usermanager.health;

import java.sql.Connection;

import javax.sql.DataSource;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class PostgresHealthCheck implements HealthIndicator {

    private final DataSource dataSource;

    public PostgresHealthCheck(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Health health() {
        try (Connection connection = dataSource.getConnection()) {
            if (connection.isValid(1)) {
                return Health.up().withDetail("database", "PostgreSQL está OK").build();
            }
        } catch (Exception e) {
            return Health.down().withDetail("erro",
                    e.getMessage()).build();
        }
        return Health.down().withDetail("database", "PostgreSQL não está respondendo").build();
    }
}
