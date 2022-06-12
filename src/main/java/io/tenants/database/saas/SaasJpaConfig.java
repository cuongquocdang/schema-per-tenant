package io.tenants.database.saas;

import lombok.RequiredArgsConstructor;
import org.hibernate.MultiTenancyStrategy;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJpaRepositories(
        basePackages = "io.tenants.database.saas",
        entityManagerFactoryRef = "saasEntityManagerFactory",
        transactionManagerRef = "saasTransactionManager")
@DependsOn("adminTransactionManager")
@RequiredArgsConstructor
public class SaasJpaConfig {

    private final DataSource dataSource;
    private final DataSourceProperties dsProperties;
    private final JpaProperties jpaProperties;
    private final MultiTenantConnectionProvider multiTenantConnectionProvider;
    private final CurrentTenantIdentifierResolver tenantIdentifierResolver;

    private JpaVendorAdapter jpaVendorAdapter() {
        return new HibernateJpaVendorAdapter();
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean saasEntityManagerFactory() {

        var entityManager = new LocalContainerEntityManagerFactoryBean();
        entityManager.setPackagesToScan(SaasJpaConfig.class.getPackage().getName());
        entityManager.setJpaVendorAdapter(this.jpaVendorAdapter());

        Map<String, Object> jpaPropertiesMap = new HashMap<>(jpaProperties.getProperties());
        jpaPropertiesMap.put(AvailableSettings.MULTI_TENANT, MultiTenancyStrategy.DATABASE);
        jpaPropertiesMap.put(AvailableSettings.MULTI_TENANT_CONNECTION_PROVIDER, multiTenantConnectionProvider);
        jpaPropertiesMap.put(AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER, tenantIdentifierResolver);
        entityManager.setJpaPropertyMap(jpaPropertiesMap);

        return entityManager;
    }

    @Bean
    @Primary
    public PlatformTransactionManager saasTransactionManager() {
        var transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(saasEntityManagerFactory().getObject());
        return transactionManager;
    }
}
