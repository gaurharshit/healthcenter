package com.gaur.healthcenter.domain.internal;

import java.time.Instant;
import lombok.Builder;
import lombok.Data;

/**
 * @author Harshit Gaur <harshit@gaurs.in>
 */
@Data
@Builder
public class HealthStatus {

    private int id;
    private String appId;
    private String url;
    private String status;
    private Instant pollTime;
    private Instant creationTime;

}
