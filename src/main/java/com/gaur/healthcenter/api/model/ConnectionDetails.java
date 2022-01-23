package com.gaur.healthcenter.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gaur.healthcenter.domain.internal.ApplicationConnectionDetails;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Harshit Gaur <harshit@gaurs.in>
 */
@Data
@NoArgsConstructor
public class ConnectionDetails {

    @JsonProperty("appId")
    private String appId;
    @JsonProperty("healthCheckUrl")
    private String healthCheckUrl;

    public ConnectionDetails(ApplicationConnectionDetails connectionDetails) {
        this.appId = connectionDetails.getAppId();
        this.healthCheckUrl = connectionDetails.getHealthCheckUrl();
    }
}
