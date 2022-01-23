package com.gaur.healthcenter.service;

import com.gaur.healthcenter.api.model.ExternalAppStatus;
import com.gaur.healthcenter.config.PollingConfiguration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * @author Harshit Gaur <harshit@gaurs.in>
 * This service polls the health check service to do health check for all the services in the database.
 */
@Service
@Log4j2
public class PollingServiceImp implements PollingService {

    private final PollingConfiguration pollingConfiguration;

    private final HealthStatusService healthStatusService;

    PollingServiceImp(PollingConfiguration pollingConfiguration, HealthStatusService healthStatusService) {
        this.pollingConfiguration = pollingConfiguration;
        this.healthStatusService = healthStatusService;
    }

    @PostConstruct
    private void initialiseService() {
        poll();
    }

    @Override
    public void poll() {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        Runnable periodicTask = this::doHealthCheck;
        executor.scheduleAtFixedRate(periodicTask, 0, pollingConfiguration.getInterval(), TimeUnit.SECONDS);
    }

    private void doHealthCheck() {
        log.debug("Scheduled health check started");
        final List<ExternalAppStatus> healthStatusForAllApplications = healthStatusService.getHealthStatusForAllApplications();
        List<String> appId = new ArrayList<>();
        for (ExternalAppStatus externalAppStatus : healthStatusForAllApplications) {
            appId.add(externalAppStatus.getAppId());
        }
        healthStatusService.checkStatus(appId);
        log.debug("Scheduled health check completed");
    }


}
