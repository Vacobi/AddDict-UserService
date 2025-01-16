package vstu.isd.userservice.config

import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container

class TestContainersConfig : ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Container
    private val postgresContainer = PostgreSQLContainer<Nothing>("postgres:latest")
        .apply {
            withDatabaseName("testdb")
            withUsername("user")
            withPassword("password")
        }

    override fun initialize(ctx: ConfigurableApplicationContext) {
        postgresContainer.start()

        System.setProperty("SPRING_DATASOURCE_URL", postgresContainer.jdbcUrl);
        System.setProperty("SPRING_DATASOURCE_USERNAME", postgresContainer.username);
        System.setProperty("SPRING_DATASOURCE_PASSWORD", postgresContainer.password);
    }
}