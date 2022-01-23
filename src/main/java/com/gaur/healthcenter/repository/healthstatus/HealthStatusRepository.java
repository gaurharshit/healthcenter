package com.gaur.healthcenter.repository.healthstatus;
import com.gaur.healthcenter.domain.internal.HealthStatus;
import java.util.List;
import java.util.Optional;
/**
 * @author Harshit Gaur <harshit@gaurs.in>
 */
public interface HealthStatusRepository {

    List<HealthStatus> getHealthStatusForAllApplications();

    Optional<HealthStatus> getHealthStatusByAppId(String id);

    HealthStatus updateStatus(HealthStatus healthStatus);

    Optional<String> getUrl(String appId);
}
