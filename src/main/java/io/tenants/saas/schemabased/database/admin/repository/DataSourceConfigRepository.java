package io.tenants.saas.schemabased.database.admin.repository;

import io.tenants.saas.schemabased.database.admin.model.DataSourceConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataSourceConfigRepository extends JpaRepository<DataSourceConfig, Long> {
}
