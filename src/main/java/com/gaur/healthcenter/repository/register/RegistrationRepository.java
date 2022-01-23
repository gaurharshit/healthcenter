package com.gaur.healthcenter.repository.register;

import com.gaur.healthcenter.domain.internal.ApplicationConnectionDetails;
/**
 * @author Harshit Gaur <harshit@gaurs.in>
 */
public interface RegistrationRepository {
    ApplicationConnectionDetails save(ApplicationConnectionDetails applicationConnectionDetails);

    boolean delete(String appId);

}
