package io.tenants.saas.schemabased.database.saas;

import io.tenants.saas.schemabased.database.admin.model.DataSourceConfig;
import io.tenants.saas.schemabased.database.admin.model.Tenant;
import io.tenants.saas.schemabased.database.admin.repository.DataSourceConfigRepository;
import io.tenants.saas.schemabased.database.admin.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class TenantConnectionProvider extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {

    private final transient  DataSource datasource;
    private final transient  DataSourceProperties dsProperties;
    private final transient  TenantRepository tenantRepository;
    private final transient  DataSourceConfigRepository dataSourceConfigRepository;

    private final Map<String, DataSource> tenantDataSources = new HashMap<>();

    private final Map<String, String> tenantDatabases = new ConcurrentHashMap<>();

    private final Map<String, String> tenantSchemas = new ConcurrentHashMap<>();

    @Override
    protected DataSource selectAnyDataSource() {
        return datasource;
    }

    @Override
    protected DataSource selectDataSource(String tenantIdentifier) {
        String database = tenantDatabases.computeIfAbsent(tenantIdentifier, this::getTenantDatabase);
        return tenantDataSources.get(database);
    }

    @Override
    public Connection getConnection(String tenantIdentifier) throws SQLException {
        log.info("Get connection for tenant {}", tenantIdentifier);
        Connection connection = super.getConnection(tenantIdentifier);
        connection.setSchema(tenantSchemas.get(tenantIdentifier));
        return connection;
    }

    @Override
    public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
        log.info("Release connection for tenant {}", tenantIdentifier);
        releaseAnyConnection(connection);
    }

    private String getTenantDatabase(String tenantIdentifier) {
        Tenant tenant = tenantRepository.findByName(tenantIdentifier)
                .orElseThrow(() -> new RuntimeException("Invalid Tenant"));
        DataSourceConfig dataSourceConfig = tenant.getDataSourceConfig();
        DataSource dataSource = tenantDataSources.computeIfAbsent(dataSourceConfig.getName(), s -> createDataSource(dataSourceConfig));
        tenantSchemas.put(tenant.getName(), tenant.getSchemaName());
        executeFlywayMigration(tenant, dataSource);
        return tenant.getDataSourceConfig().getName();
    }

    @PostConstruct
    public void configureDataSources() {
        List<DataSourceConfig> dataSourceConfigs = dataSourceConfigRepository.findAll();
        dataSourceConfigs.forEach(dataSourceConfig -> tenantDataSources.put(dataSourceConfig.getName(), createDataSource(dataSourceConfig)));
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private DataSource createDataSource(DataSourceConfig dataSourceConfig) {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create(this.getClass().getClassLoader())
                .driverClassName(dsProperties.getDriverClassName())
                .url(dataSourceConfig.getDatabaseUrl())
                .username(dataSourceConfig.getUsername())
                .password(dataSourceConfig.getPassword());
        if (dsProperties.getType() != null) {
            dataSourceBuilder.type(dsProperties.getType());
        }
        return dataSourceBuilder.build();
    }

    private void executeFlywayMigration(Tenant tenant, DataSource dataSource) {
        String schema = tenant.getSchemaName();
        log.info("Migrating {} schema: db/migration/tenants", schema);
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .schemas(schema)
                .locations("db/migration/tenants")
                .baselineOnMigrate(true)
                .load();
        flyway.migrate();
    }

}
