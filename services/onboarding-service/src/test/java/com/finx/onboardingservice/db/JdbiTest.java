package com.finx.onboardingservice.db;

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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public abstract class JdbiTest {

    protected Jdbi jdbi;
    private Handle handle;
    private Liquibase liquibase;

    @BeforeEach
    void setUp() throws Exception {
        Environment environment = new Environment("test-env");
        BootstrapLogging.bootstrap(Level.INFO);
        final JdbiFactory factory = new JdbiFactory();
        jdbi = factory.build(environment, getDataSourceFactory(), "test");
        jdbi.installPlugin(new Jackson2Plugin());
        handle = jdbi.open();
        migrateDatabase();
    }

    @AfterEach
    void tearDown() throws Exception {
        liquibase.dropAll();
    }

    private DataSourceFactory getDataSourceFactory() {
        DataSourceFactory dataSourceFactory = new DataSourceFactory();
        dataSourceFactory.setDriverClass("org.h2.Driver");
        dataSourceFactory.setUrl(String.format(
                "jdbc:h2:mem:test-%s;MODE=PostgreSQL;TRACE_LEVEL_FILE=3",   //this is for in-memory db (*)
                System.currentTimeMillis()));
        dataSourceFactory.setUser("sa");
        return dataSourceFactory;
    }

    private void migrateDatabase() throws LiquibaseException {
        liquibase = new Liquibase("migrations.xml", new ClassLoaderResourceAccessor(),
                new JdbcConnection(handle.getConnection()));
        liquibase.update("");
    }
}
