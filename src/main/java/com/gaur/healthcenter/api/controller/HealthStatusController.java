package com.gaur.healthcenter.api.controller;

import com.gaur.healthcenter.api.model.Applications;
import com.gaur.healthcenter.api.model.ExternalAppStatus;
import com.gaur.healthcenter.domain.internal.HealthStatus;
import com.gaur.healthcenter.service.HealthStatusService;
import com.gaur.healthcenter.service.exception.AppUrlInfoMissingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * @author Harshit Gaur <harshit@gaurs.in>
 * This controller provides the APIs for external entity to interact with HealthStatusService
 */
@RestController
@Log4j2
@RequestMapping("${application.healthcenter.rest.uribase:/rest}")
public class HealthStatusController {

    private HealthStatusService healthStatusService;

    public HealthStatusController(HealthStatusService healthStatusService) {
        this.healthStatusService = healthStatusService;
    }

    @GetMapping(value = "/api/v1/applications", produces = MediaType.APPLICATION_JSON_VALUE)
    public Applications getApplicationsInfo() {
        log.debug("Incoming request to get all application status");
        return Applications.builder().externalAppStatus(healthStatusService.getHealthStatusForAllApplications())
            .build();
    }

    @GetMapping(value = "/api/v1/application", produces = MediaType.APPLICATION_JSON_VALUE, params = {"appId"})
    public ExternalAppStatus getApplicationInfoByAppId(@RequestParam("appId") String appId) {
        return healthStatusService.getHealthStatusByAppId(appId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No application with id=" + appId));
    }

    @PostMapping(value = "/api/v1/application/checkStatus",  params = {
        "appId"})
    public void doHealthCheck(@RequestParam("appId") String appId) {
        try {
            healthStatusService.checkStatus(Collections.singletonList(appId));
        } catch (AppUrlInfoMissingException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No applicaiton with id=" + appId);
        }

    }

    }
