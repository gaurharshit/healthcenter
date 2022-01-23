package com.gaur.healthcenter.config;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * @author Harshit Gaur <harshit@gaurs.in>
 * This initialises the NamedParameterJdbcTemplate with the configured properties. This also creates a table with
 * pre-set values.
 */
@Data
@Configuration
@ConfigurationProperties("application.datasource")
@Log4j2
public class DatabaseConfiguration {

    private String url;
    private String driver;
    private String username;
    private String password;

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate() throws SQLException {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        if (!isDatabaseAlreadyInitialised(dataSource)) {
            populateDatabase(jdbcTemplate);
        }
        return jdbcTemplate;
    }

    private void populateDatabase(NamedParameterJdbcTemplate jdbcTemplate) {
        jdbcTemplate.getJdbcTemplate().execute(
            "CREATE TABLE health_status (id INT AUTO_INCREMENT, app_id VARCHAR2(64) NOT NULL PRIMARY KEY, url VARCHAR2(64) NOT NULL, poll_time TIMESTAMP, registration_time TIMESTAMP, status VARCHAR2(24))");
        jdbcTemplate.getJdbcTemplate().execute(
            "INSERT INTO health_status (app_id, url, poll_time, status,registration_time)VALUES ('health_service', 'http://localhost:8080/actuator/health', {ts '2022-01-20 07:23:48.114'}, 'DOWN', {ts '2022-01-20 07:23:48.112'})");
        jdbcTemplate.getJdbcTemplate().execute(
            "INSERT INTO health_status (app_id, url, poll_time, status,registration_time) VALUES ('appointment_scheduler', 'http://localhost:8081/appointment-scheduler/manage/health', {ts '2022-01-20 07:23:48.112'}, 'UP', {ts '2022-01-20 07:23:48.112'});");
        jdbcTemplate.getJdbcTemplate().execute(
            "INSERT INTO health_status (app_id, url, poll_time, status,registration_time) VALUES ('customer_service', 'http://localhost:8082/customer-service/manage/health', {ts '2022-01-20 07:23:48.113'}, 'DOWN', {ts '2022-01-20 07:23:48.112'});");
        jdbcTemplate.getJdbcTemplate().execute(
            "INSERT INTO health_status (app_id, url, poll_time, status,registration_time)VALUES ('doctor_service', 'http://localhost:8083/doctor-service/manage/health', {ts '2022-01-20 07:23:48.114'}, 'UP', {ts '2022-01-20 07:23:48.112'})");
    }

    private boolean isDatabaseAlreadyInitialised(DataSource dataSource) throws SQLException {
        try (var connection = dataSource.getConnection()) {
            DatabaseMetaData dbm = connection.getMetaData();
            ResultSet tables = dbm.getTables("", "PUBLIC", "HEALTH_STATUS", new String[]{"TABLE"});
            return tables.next();
        }

    }
}
