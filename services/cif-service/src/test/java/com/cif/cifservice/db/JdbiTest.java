package com.cif.cifservice.db;

import ch.qos.logback.classic.Level;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.logging.BootstrapLogging;
import io.dropwizard.setup.Environment;
import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.jackson2.Jackson2Plugin;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

public abstract class JdbiTest {

    private final Logger logger = LoggerFactory.getLogger(JdbiTest.class);
    protected static Jdbi jdbi;
    private static PostgreSQLContainer<?> POSTGRES_CONTAINER;


    @BeforeAll
    static void beforeAll() throws Exception {
        POSTGRES_CONTAINER = new PostgreSQLContainer<>(DockerImageName.parse("postgres:13.3"));
        POSTGRES_CONTAINER.start();
        Environment environment = new Environment("test-env");
        BootstrapLogging.bootstrap(Level.INFO);
        final JdbiFactory factory = new JdbiFactory();
        jdbi = factory.build(environment, getDataSourceFactory(), "test");
        jdbi.installPlugin(new Jackson2Plugin());
        migrateDatabase(jdbi);
    }

    @AfterAll
    static void afterAll() {
        POSTGRES_CONTAINER.stop();
    }

    private static DataSourceFactory getDataSourceFactory() {
        DataSourceFactory dataSourceFactory = new DataSourceFactory();
        dataSourceFactory.setDriverClass(POSTGRES_CONTAINER.getDriverClassName());
        dataSourceFactory.setUrl(POSTGRES_CONTAINER.getJdbcUrl());
        dataSourceFactory.setUser(POSTGRES_CONTAINER.getUsername());
        dataSourceFactory.setPassword(POSTGRES_CONTAINER.getPassword());
        return dataSourceFactory;
    }

    private static void migrateDatabase(Jdbi jdbi) throws LiquibaseException {
        Handle handle = jdbi.open();
        Liquibase liquibase = new Liquibase("migrations.xml",
                new ClassLoaderResourceAccessor(),
                new JdbcConnection(handle.getConnection()));
        liquibase.update("");
    }
}
