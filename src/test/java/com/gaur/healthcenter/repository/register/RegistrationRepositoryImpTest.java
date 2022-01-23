package com.gaur.healthcenter.repository.register;

import com.gaur.healthcenter.domain.internal.ApplicationConnectionDetails;
import com.gaur.healthcenter.domain.internal.HealthStatus;
import com.gaur.healthcenter.repository.common.AbstractRepositoryBuilder;
import com.gaur.healthcenter.repository.healthstatus.HealthStatusRepository;
import com.gaur.healthcenter.repository.healthstatus.HealthStatusRepositoryImp;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Harshit Gaur <harshit@gaurs.in>
 */
class RegistrationRepositoryImpTest extends AbstractRepositoryBuilder {

    private RegistrationRepository registrationRepository;
    private HealthStatusRepository healthStatusRepository;

    RegistrationRepositoryImpTest() {
        registrationRepository = new RegistrationRepositoryImp(jdbcTemplate);
        healthStatusRepository = new HealthStatusRepositoryImp(jdbcTemplate);
    }

    @Test
    void shouldInsertNewApplicationInfo() {
        String appId = "test";
        String url = "https://localhost:8091/test/manage/health";

        registrationRepository.save(ApplicationConnectionDetails.builder().appId(appId).healthCheckUrl(url).build());

        final Optional<HealthStatus> healthStatus = healthStatusRepository.getHealthStatusByAppId(appId);

        Assertions.assertEquals(appId, healthStatus.get().getAppId());
        Assertions.assertEquals(url, healthStatus.get().getUrl());

    }

    @Test
    void delete() {
        String appId = "customer_service";

        registrationRepository.delete(appId);

        final Optional<HealthStatus> healthStatus = healthStatusRepository.getHealthStatusByAppId(appId);

        Assertions.assertTrue(healthStatus.isEmpty());
    }
}