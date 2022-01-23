package com.gaur.healthcenter.api.controller;

import com.gaur.healthcenter.api.model.ConnectionDetails;
import com.gaur.healthcenter.service.RegistrationService;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Harshit Gaur <harshit@gaurs.in>
 * This controller provides the APIs for external entity to interact with RegistrationService
 */
@RestController
@RequestMapping("${application.healthcenter.rest.uribase:/rest}")
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping(value = "/api/v1/register", produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE)
    public ConnectionDetails registerApplication(
        @RequestBody @Validated ConnectionDetails connectionDetails) {
        return registrationService.save(connectionDetails);
    }

    @DeleteMapping(value = "/api/v1/remove", params = {"appId"})
    public boolean removeApplication(@RequestParam("appId") String appId) {
        return registrationService.delete(appId);
    }

}
