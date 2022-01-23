package com.gaur.healthcenter.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Harshit Gaur <harshit@gaurs.in>
 */
@Configuration
@ConfigurationProperties("application.healthcheck")
@Data
public class PollingConfiguration {

    private int interval = 10;
}
