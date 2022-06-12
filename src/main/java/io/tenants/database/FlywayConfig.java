package io.tenants.database;

import io.tenants.database.saas.TenantSchemaResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class FlywayConfig {

    private static final String DEFAULT_LOCATION = "db/migration/default";

    private final DataSource dataSource;

    @Bean
    public Flyway flyway() {
        log.info("Migrating default schema: {}", DEFAULT_LOCATION);
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .schemas(TenantSchemaResolver.DEFAULT_SCHEMA)
                .locations(DEFAULT_LOCATION)
                .baselineOnMigrate(true)
                .load();
        flyway.migrate();
        return flyway;
    }

}
