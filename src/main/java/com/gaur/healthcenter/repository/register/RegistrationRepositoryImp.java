package com.gaur.healthcenter.repository.register;

import com.gaur.healthcenter.domain.internal.ApplicationConnectionDetails;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.Instant;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
/**
 * @author Harshit Gaur <harshit@gaurs.in>
 * This supports the RegistrationService to do database related functions.
 */
@Repository
@Log4j2
public class RegistrationRepositoryImp implements RegistrationRepository {

    private static final String HEALTH_TABLE = "health_status";
    private static final String COLUMN_APP_ID = "app_id";
    private static final String COLUMN_URL = "url";
    private static final String COLUMN_REGISTRATION_TIME = "registration_time";

    private static final String INSERT_SQL = "INSERT INTO " + HEALTH_TABLE
        + "( " + COLUMN_APP_ID + ","
        + COLUMN_REGISTRATION_TIME + ","
        + COLUMN_URL + ")"
        + " VALUES ("
        + ":" + COLUMN_APP_ID + ","
        + ":" + COLUMN_REGISTRATION_TIME + ","
        + ":" + COLUMN_URL + ")";
    private static final String DELETE_SQL = "DELETE FROM " + HEALTH_TABLE +
        " WHERE " + COLUMN_APP_ID + "=:" + COLUMN_APP_ID;

    private static final String GET_SQL = "SELECT " + COLUMN_APP_ID + " FROM " + HEALTH_TABLE +
        " WHERE " + COLUMN_APP_ID + "=:" + COLUMN_APP_ID;

    private static final String UPDATE_SQL = "UPDATE " + HEALTH_TABLE +
        " SET " +
        COLUMN_URL + "=:" + COLUMN_URL + ","
        + COLUMN_REGISTRATION_TIME + "=:" + COLUMN_REGISTRATION_TIME +
        " WHERE " +
        COLUMN_APP_ID + "=:" + COLUMN_APP_ID;


    private NamedParameterJdbcTemplate jdbcTemplate;

    RegistrationRepositoryImp(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ApplicationConnectionDetails save(ApplicationConnectionDetails applicationConnectionDetails) {
        if (!isAppicationRegisteredAlready(applicationConnectionDetails.getAppId())) {
            log.debug("Creating new application entry in the database with app_id:{} and url:{}",
                applicationConnectionDetails.getAppId(), applicationConnectionDetails.getHealthCheckUrl());

            insertApplicationInfo(applicationConnectionDetails);

            log.debug("Creation for app_id:{} successful", applicationConnectionDetails.getAppId());

            return applicationConnectionDetails;
        } else {
            log.debug("Updating application entry in the database with app_id:{} and url:{}",
                applicationConnectionDetails.getAppId(), applicationConnectionDetails.getHealthCheckUrl());
            updateApplicationInfo(applicationConnectionDetails);
            log.debug("Update for app_id:{} successful", applicationConnectionDetails.getAppId());
        }
        return applicationConnectionDetails;
    }

    private void insertApplicationInfo(ApplicationConnectionDetails applicationConnectionDetails) {
        jdbcTemplate.update(INSERT_SQL, new MapSqlParameterSource()
            .addValue(COLUMN_APP_ID, applicationConnectionDetails.getAppId(), Types.VARCHAR)
            .addValue(COLUMN_REGISTRATION_TIME, Timestamp.from(Instant.now()), Types.TIMESTAMP)
            .addValue(COLUMN_URL, applicationConnectionDetails.getHealthCheckUrl(), Types.VARCHAR));
    }

    private void updateApplicationInfo(ApplicationConnectionDetails applicationConnectionDetails) {
        final int rows = jdbcTemplate.update(UPDATE_SQL, new MapSqlParameterSource()
            .addValue(COLUMN_APP_ID, applicationConnectionDetails.getAppId(), Types.VARCHAR)
            .addValue(COLUMN_REGISTRATION_TIME, Timestamp.from(Instant.now()), Types.TIMESTAMP)
            .addValue(COLUMN_URL, applicationConnectionDetails.getHealthCheckUrl(), Types.VARCHAR));
        if (rows != 1) {
            throw new IncorrectResultSizeDataAccessException(1);
        }
    }

    private boolean isAppicationRegisteredAlready(String appId) {
        try {
            jdbcTemplate.queryForObject(GET_SQL,
                new MapSqlParameterSource(COLUMN_APP_ID, appId),
                this::getAppIdFromResultSet);
            return true;
        } catch (IncorrectResultSizeDataAccessException e) {
            log.debug("No record found in database for appid:{} ", appId);
            return false;
        }
    }

    @Override
    public boolean delete(String appId) {
        return jdbcTemplate.update(DELETE_SQL, new MapSqlParameterSource(COLUMN_APP_ID, appId)) > 0;
    }

    private String getAppIdFromResultSet(ResultSet rs, int rowNum) throws SQLException {
        return rs.getString(COLUMN_APP_ID);
    }
}
