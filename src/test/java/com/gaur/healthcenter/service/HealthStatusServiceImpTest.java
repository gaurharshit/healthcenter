package com.gaur.healthcenter.service;

import com.gaur.healthcenter.repository.healthstatus.HealthStatusRepository;
import com.gaur.healthcenter.repository.healthstatus.HealthStatusRepositoryImp;
import com.gaur.healthcenter.service.exception.AppUrlInfoMissingException;
import java.util.Collections;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * @author Harshit Gaur <harshit@gaurs.in>
 */
@ExtendWith(MockitoExtension.class)
class HealthStatusServiceImpTest {

    @Mock
    private HealthStatusRepository healthStatusRepository;
    private HealthStatusService healthStatusService;

    HealthStatusServiceImpTest() {
        this.healthStatusRepository = Mockito.mock(HealthStatusRepositoryImp.class);
        healthStatusService = new HealthStatusServiceImp(healthStatusRepository);
    }

    @Test
    void shouldThrowAppInfoMissingExceptionWhenCheckStatusWithWrongId() {
        Assertions.assertThrows(AppUrlInfoMissingException.class,
            () -> healthStatusService.checkStatus(Collections.singletonList("test")));
    }
}