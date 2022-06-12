package io.tenants.saas.schemabased;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SchemaBasedMultiTenantsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SchemaBasedMultiTenantsApplication.class, args);
	}

}
