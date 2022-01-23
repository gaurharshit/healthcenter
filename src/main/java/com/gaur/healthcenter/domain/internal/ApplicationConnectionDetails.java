package com.gaur.healthcenter.domain.internal;

import lombok.Builder;
import lombok.Data;

/**
 * @author Harshit Gaur <harshit@gaurs.in>
 */
@Data
@Builder
public class ApplicationConnectionDetails {

    String appId;
    String healthCheckUrl;


}
