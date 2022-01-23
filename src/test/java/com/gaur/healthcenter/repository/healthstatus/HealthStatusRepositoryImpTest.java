package com.gaur.healthcenter.repository.healthstatus;

import com.gaur.healthcenter.domain.internal.HealthStatus;
import com.gaur.healthcenter.repository.common.AbstractRepositoryBuilder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Harshit Gaur <harshit@gaurs.in>
 */
class HealthStatusRepositoryImpTest extends AbstractRepositoryBuilder {

    private final HealthStatusRepository healthStatusRepository;

    HealthStatusRepositoryImpTest() {
        healthStatusRepository = new HealthStatusRepositoryImp(jdbcTemplate);
    }

    @Test
    void shouldGetHealthStatusForAllApplications() throws ParseException {
        List<HealthStatus> expectedHealthStatuses = getExpectedHealthStatuses();

        final List<HealthStatus> healthStatusForAllApplications = healthStatusRepository.getHealthStatusForAllApplications();

        Assertions.assertEquals(expectedHealthStatuses, healthStatusForAllApplications);
    }

    @Test
    void shouldGetHealthStatusByAppId() throws ParseException {
        final HealthStatus expectedHealthStatus = buildExpectedHealthStatus(1, "appointment_scheduler", "UP",
            getFormattedTime("2022-01-20 07:23:48.112"),
            "https://localhost:8081/appointment-scheduler/manage/health", getFormattedTime("2022-01-20 07:23:48.112"));

        final Optional<HealthStatus> appHealthStatus = healthStatusRepository.getHealthStatusByAppId(
            "appointment_scheduler");

        Assertions.assertEquals(expectedHealthStatus, appHealthStatus.get());
    }

    private Instant getFormattedTime(String time) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(time).toInstant();
    }

    @Test
    void shouldGetEmptyHealthStatus() {
        final Optional<HealthStatus> appHealthStatus = healthStatusRepository.getHealthStatusByAppId(
            "N/A");

        Assertions.assertTrue(appHealthStatus.isEmpty());

    }

    @Test
    void shouldGetUrlForApp() {
        String expectedUrl = "https://localhost:8081/appointment-scheduler/manage/health";

        final Optional<String> url = healthStatusRepository.getUrl("appointment_scheduler");

        Assertions.assertEquals(expectedUrl, url.get());
    }

    @Test
    void shouldGetEmptyUrlForUnregisteredApp() {
        final Optional<String> url = healthStatusRepository.getUrl("N/A");
        Assertions.assertTrue(url.isEmpty());
    }

    List<HealthStatus> getExpectedHealthStatuses() throws ParseException {
        List<HealthStatus> expectedHealthStatuses = new ArrayList<>();
        expectedHealthStatuses.add(buildExpectedHealthStatus(1, "appointment_scheduler", "UP",
            getFormattedTime("2022-01-20 07:23:48.112"), "https://localhost:8081/appointment-scheduler/manage/health",
            getFormattedTime("2022-01-20 07:23:48.112")));
        expectedHealthStatuses.add(buildExpectedHealthStatus(2, "customer_service", "DOWN",
            getFormattedTime("2022-01-20 07:23:48.113"), "https://localhost:8082/customer-service/manage/health",
            getFormattedTime("2022-01-20 07:23:48.112")));
        expectedHealthStatuses.add(buildExpectedHealthStatus(3, "doctor_service", "UP",
            getFormattedTime("2022-01-20 07:23:48.114"), "https://localhost:8083/doctor-service/manage/health",
            getFormattedTime("2022-01-20 07:23:48.112")));

        return expectedHealthStatuses;
    }

    HealthStatus buildExpectedHealthStatus(int id, String appId, String status, Instant pollTime, String url,
        Instant creationTime) {
        return HealthStatus.builder().id(id).appId(appId).status(status).pollTime(pollTime).url(url)
            .creationTime(creationTime).build();
    }
}