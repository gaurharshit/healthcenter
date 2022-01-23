package com.gaur.healthcenter.service;

import com.gaur.healthcenter.api.model.ConnectionDetails;

/**
 * @author Harshit Gaur <harshit@gaurs.in>
 * This interface is intended to provide registration and removal of applications from the database.
 */
public interface RegistrationService {

    ConnectionDetails save(ConnectionDetails connectionDetails);

    boolean delete(String appId);

}
