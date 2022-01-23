package com.gaur.healthcenter.repository.healthstatus;

import com.gaur.healthcenter.domain.internal.HealthStatus;
import com.gaur.healthcenter.repository.common.AbstractRepositoryBuilder;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

/**
 * @author Harshit Gaur <harshit@gaurs.in>
 */
class HealthStatusRepositryUpdateTest extends AbstractRepositoryBuilder {

    private final HealthStatusRepository healthStatusRepository;

    HealthStatusRepositryUpdateTest() {
        healthStatusRepository = new HealthStatusRepositoryImp(jdbcTemplate);
    }

    @Test
    void shouldUpdateStateToUpFromUp() {
        String expectedStatus = "UP";
        String appId = "appointment_scheduler";

        healthStatusRepository.updateStatus(HealthStatus.builder().appId(appId).status(expectedStatus).build());
        final Optional<HealthStatus> healthStatusByAppId = healthStatusRepository.getHealthStatusByAppId(appId);

        Assertions.assertEquals(expectedStatus, healthStatusByAppId.get().getStatus());
    }


    @Test
    void shouldUpdateStateToUpFromDown() {
        String expectedStatus = "DOWN";
        String appId = "customer_service";

        healthStatusRepository.updateStatus(HealthStatus.builder().appId(appId).status(expectedStatus).build());
        final Optional<HealthStatus> healthStatusByAppId = healthStatusRepository.getHealthStatusByAppId(appId);

        Assertions.assertEquals(expectedStatus, healthStatusByAppId.get().getStatus());
    }

    @Test
    void shouldThrowIncorrectUpdateStateToUpFromDown() {
        String expectedStatus = "DOWN";
        String appId = "NA_service";

        Assertions.assertThrows(IncorrectResultSizeDataAccessException.class, () -> healthStatusRepository.updateStatus(
            HealthStatus.builder().appId(appId).status(expectedStatus).build()));
    }
}
