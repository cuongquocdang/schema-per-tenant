package io.tenants.database.admin.model;

import io.tenants.database.saas.TenantSchemaResolver;
import io.tenants.database.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "data_source_configs", schema = TenantSchemaResolver.DEFAULT_SCHEMA)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class DataSourceConfig extends BaseEntity {

    @Column(unique = true)
    private String name;

    @Column(name = "database_url")
    private String databaseUrl;

    @Column
    private String username;

    @Column
    private String password;

    @Column
    private boolean initialize;
}
