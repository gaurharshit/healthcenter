package com.gaur.healthcenter.repository.common;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

/**
 * @author Harshit Gaur <harshit@gaurs.in>
 */
public abstract class AbstractRepositoryBuilder {

    protected static EmbeddedDatabase dataSource;
    protected static NamedParameterJdbcTemplate jdbcTemplate;

    @BeforeAll
    public static void setUp() {
        dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
            .addScript("repository/h2-health-schema.sql")
            .build();
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @AfterAll
    public static void shutdown() {
        dataSource.shutdown();
    }

}
