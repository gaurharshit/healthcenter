package com.gaur.healthcenter.repository.healthstatus;

import com.gaur.healthcenter.domain.internal.HealthStatus;
import com.gaur.healthcenter.domain.internal.HealthStatus.HealthStatusBuilder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author Harshit Gaur <harshit@gaurs.in>
 * This supports the healthStatusService to do database related functions.
 */
@Repository
@Log4j2
public class HealthStatusRepositoryImp implements HealthStatusRepository {

    private static final String HEALTH_TABLE = "health_status";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_POLL_TIME = "poll_time";
    private static final String COLUMN_APP_ID = "app_id";
    private static final String COLUMN_URL = "url";
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_REGISTRATION_TIME = "registration_time";

    private static final String GET_ALL_SQL = "SELECT " + COLUMN_ID + "," +
        COLUMN_POLL_TIME + "," +
        COLUMN_APP_ID + "," +
        COLUMN_URL + "," +
        COLUMN_STATUS + "," +
        COLUMN_REGISTRATION_TIME +
        " FROM " + HEALTH_TABLE;
    private static final String SELECT_ALL_USING_APP_ID_SQL = "SELECT " + COLUMN_ID + "," +
        COLUMN_POLL_TIME + "," +
        COLUMN_APP_ID + "," +
        COLUMN_URL + "," +
        COLUMN_STATUS + "," +
        COLUMN_REGISTRATION_TIME +
        " FROM " + HEALTH_TABLE +
        " WHERE " + COLUMN_APP_ID + "=:" + COLUMN_APP_ID;

    private static final String UPDATE_STATUS_FOR_APP_SQL = "UPDATE " + HEALTH_TABLE +
        " SET " +
        COLUMN_STATUS + "=:" + COLUMN_STATUS + "," +
        COLUMN_POLL_TIME + "=:" + COLUMN_POLL_TIME +
        " WHERE " +
        COLUMN_APP_ID + "=:" + COLUMN_APP_ID;

    private static final String GET_URL_USING_APP_ID = "SELECT " + COLUMN_URL +
        " FROM " + HEALTH_TABLE +
        " WHERE " + COLUMN_APP_ID + "=:" + COLUMN_APP_ID;


    private NamedParameterJdbcTemplate jdbcTemplate;

    public HealthStatusRepositoryImp(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<HealthStatus> getHealthStatusForAllApplications() {
        return jdbcTemplate.query(GET_ALL_SQL, new MapSqlParameterSource(),
            this::fromResultSetToHealthStatus);
    }

    @Override
    public Optional<HealthStatus> getHealthStatusByAppId(String appId) {
        return optionalResult(
            () -> jdbcTemplate.queryForObject(SELECT_ALL_USING_APP_ID_SQL,
                new MapSqlParameterSource(COLUMN_APP_ID, appId),
                this::fromResultSetToHealthStatus));
    }

    @Override
    public HealthStatus updateStatus(HealthStatus healthStatus) {
        int rows = jdbcTemplate.update(UPDATE_STATUS_FOR_APP_SQL, new MapSqlParameterSource()
            .addValue(COLUMN_STATUS, healthStatus.getStatus(), Types.VARCHAR)
            .addValue(COLUMN_POLL_TIME, Timestamp.from(Instant.now()), Types.TIMESTAMP)
            .addValue(COLUMN_APP_ID, healthStatus.getAppId(), Types.VARCHAR));
        if (rows != 1) {
            throw new IncorrectResultSizeDataAccessException(1);
        }
        return healthStatus;
    }

    @Override
    public Optional<String> getUrl(String appId) {
        return optionalUrl(() -> jdbcTemplate.queryForObject(GET_URL_USING_APP_ID,
            new MapSqlParameterSource(COLUMN_APP_ID, appId),
            this::fromResultSetToUrl));

    }

    private HealthStatus fromResultSetToHealthStatus(ResultSet rs, int rowNum) throws SQLException {
        final HealthStatusBuilder healthStatusBuilder = HealthStatus.builder().id(rs.getInt(COLUMN_ID))
            .appId(rs.getString(COLUMN_APP_ID))
            .url(rs.getString(COLUMN_URL))
            .creationTime(Instant.ofEpochMilli(rs.getTimestamp(COLUMN_REGISTRATION_TIME).getTime()))
            .status(rs.getString(COLUMN_STATUS));
        return checkAndAddPollTime(rs, healthStatusBuilder);

    }

    private HealthStatus checkAndAddPollTime(ResultSet rs, HealthStatusBuilder builder) throws SQLException {
        if (Objects.isNull(rs.getString(COLUMN_POLL_TIME))) {
            return builder.build();
        } else {
            return builder.pollTime(Instant.ofEpochMilli(rs.getTimestamp(COLUMN_POLL_TIME).getTime())).build();
        }
    }

    private String fromResultSetToUrl(ResultSet rs, int rowNum) throws SQLException {
        return rs.getString(COLUMN_URL);
    }


    private Optional<HealthStatus> optionalResult(Supplier<HealthStatus> query) {
        try {
            HealthStatus healthStatus = query.get();
            return Optional.of(healthStatus);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private Optional<String> optionalUrl(Supplier<String> query) {
        try {
            final String url = query.get();
            return Optional.of(url);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
