package com.gaur.healthcenter.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.gaur.healthcenter.domain.internal.HealthStatus;
import java.time.Instant;
import java.util.Objects;
import lombok.Data;

/**
 * @author Harshit Gaur <harshit@gaurs.in>
 */
@Data
@JsonInclude(Include.NON_NULL)
public class ExternalAppStatus {

    private String appId;
    private String url;
    private String status;
    private Instant pollTime;
    private Instant creationTime;

    public ExternalAppStatus(HealthStatus healthStatus) {
        this.appId = healthStatus.getAppId();
        this.url = healthStatus.getUrl();
        this.status = healthStatus.getStatus();
        this.pollTime = healthStatus.getPollTime();
        this.creationTime = healthStatus.getCreationTime();
    }

    public String getStatus() {
        if (Objects.isNull(status)) {
            return String.valueOf(AppStatus.UNAVAILABLE);
        }
        return status;
    }

    public enum AppStatus {
        UNAVAILABLE,
        UP,
        DOWN
    }


}
