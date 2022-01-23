package com.gaur.healthcenter.service;

import com.gaur.healthcenter.api.model.ExternalAppStatus;
import com.gaur.healthcenter.domain.internal.HealthStatus;
import java.util.List;
import java.util.Optional;

/**
 * @author Harshit Gaur <harshit@gaurs.in>
 * This interface is intended to handle the health check for all the services available in the database.
 */
public interface HealthStatusService {

    List<ExternalAppStatus> getHealthStatusForAllApplications();

    Optional<ExternalAppStatus> getHealthStatusByAppId(String id);

    void checkStatus(List<String> appId);

}
