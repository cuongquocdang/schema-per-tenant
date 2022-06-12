package io.tenants.saas.schemabased.database.admin.model;

import io.tenants.saas.schemabased.database.BaseEntity;
import io.tenants.saas.schemabased.database.saas.TenantSchemaResolver;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "tenants", schema = TenantSchemaResolver.DEFAULT_SCHEMA)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Tenant extends BaseEntity {

    @Column(length = 100, unique = true)
    private String name;

    @Column
    private String url;

    @Column(name = "schema_name")
    private String schemaName;

    /**
     * Name of the tenant to which the user belongs
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "datasource_config_id")
    private DataSourceConfig dataSourceConfig;
}
