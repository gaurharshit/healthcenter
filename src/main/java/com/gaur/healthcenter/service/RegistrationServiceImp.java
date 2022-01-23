package com.gaur.healthcenter.service;

import com.gaur.healthcenter.api.model.ConnectionDetails;
import com.gaur.healthcenter.domain.internal.ApplicationConnectionDetails;
import com.gaur.healthcenter.repository.register.RegistrationRepository;
import org.springframework.stereotype.Service;

/**
 * @author Harshit Gaur <harshit@gaurs.in>
 * This service provides the registration and removal of applications from the database.
 */
@Service
public class RegistrationServiceImp implements RegistrationService {


    RegistrationRepository registrationRepository;

    RegistrationServiceImp(RegistrationRepository registrationRepository) {
        this.registrationRepository = registrationRepository;
    }

    @Override
    public ConnectionDetails save(ConnectionDetails connectionDetails) {
        return new ConnectionDetails(registrationRepository.save(
            ApplicationConnectionDetails.builder().appId(connectionDetails.getAppId())
                .healthCheckUrl(connectionDetails.getHealthCheckUrl()).build()));
    }

    @Override
    public boolean delete(String appId) {
        return registrationRepository.delete(appId);
    }

}
