package io.tenants.database.admin;

import lombok.RequiredArgsConstructor;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableJpaRepositories(
        basePackages = "io.tenants.database.admin",
        entityManagerFactoryRef = "adminEntityManagerFactory",
        transactionManagerRef = "adminTransactionManager")
@RequiredArgsConstructor
public class AdminJpaConfig {

    private final JpaProperties jpaProperties;
    private final DataSource dataSource;

    private JpaVendorAdapter jpaVendorAdapter() {
        return new HibernateJpaVendorAdapter();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean adminEntityManagerFactory() {
        var entityManager = new LocalContainerEntityManagerFactoryBean();
        entityManager.setPackagesToScan(AdminJpaConfig.class.getPackage().getName());
        entityManager.setJpaVendorAdapter(this.jpaVendorAdapter());
        entityManager.setDataSource(dataSource);

        var jpaPropertiesMap = new HashMap<>(jpaProperties.getProperties());
        jpaPropertiesMap.put(AvailableSettings.SHOW_SQL, "true");
        jpaPropertiesMap.put(AvailableSettings.HBM2DDL_AUTO, "update");
        entityManager.setJpaPropertyMap(jpaPropertiesMap);

        return entityManager;
    }

    @Bean
    public PlatformTransactionManager adminTransactionManager() {
        var transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(adminEntityManagerFactory().getObject());
        return transactionManager;
    }

}
