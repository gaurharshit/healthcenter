package com.gaur.healthcenter.service;

import com.gaur.healthcenter.api.model.ExternalAppStatus;
import com.gaur.healthcenter.domain.client.response.HealthCheckResponse;
import com.gaur.healthcenter.domain.internal.HealthStatus;
import com.gaur.healthcenter.repository.healthstatus.HealthStatusRepository;
import com.gaur.healthcenter.service.exception.AppUrlInfoMissingException;
import com.gaur.healthcenter.service.exception.ApplicationHealthEndpointUnreachableException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * @author Harshit Gaur <harshit@gaurs.in>
 * This service handles the health check for all the services available in the database by getting the url.
 * This service also handles the get requests for all the applications present in the database.
 */
@Service
@Log4j2
public class HealthStatusServiceImp implements HealthStatusService {

    private final HealthStatusRepository healthStatusRepository;

    HealthStatusServiceImp(HealthStatusRepository healthStatusRepository) {
        this.healthStatusRepository = healthStatusRepository;
    }

    @Override
    public List<ExternalAppStatus> getHealthStatusForAllApplications() {
        final List<HealthStatus> healthStatusForAllApplications = healthStatusRepository.getHealthStatusForAllApplications();
        return healthStatusForAllApplications.stream().map(this::convertToModel).collect(Collectors.toList());
    }

    @Override
    public Optional<ExternalAppStatus> getHealthStatusByAppId(String id) {
        final Optional<HealthStatus> healthStatus = healthStatusRepository.getHealthStatusByAppId(id);
        return healthStatus.map(ExternalAppStatus::new);
    }

    @Override
    public void checkStatus(List<String> appId) {
        for (String app : appId) {
            try {
                checkApplicationStatus(app).subscribe(healthCheckResponse -> {
                    healthStatusRepository.updateStatus(
                        HealthStatus.builder().appId(app).status(healthCheckResponse.getStatus()).build());
                });
            } catch (ApplicationHealthEndpointUnreachableException e) {
                log.warn("Application health check url not reachable:{}", appId);
                healthStatusRepository.updateStatus(HealthStatus.builder().appId(app).status("DOWN").build());
            }
        }

    }

    ExternalAppStatus convertToModel(HealthStatus healthStatus) {
        return new ExternalAppStatus(healthStatus);
    }

    private Mono<HealthCheckResponse> checkApplicationStatus(String appId) {
        final Optional<String> appUrl = healthStatusRepository.getUrl(appId);
        if (appUrl.isEmpty()) {
            throw new AppUrlInfoMissingException("Application Url is missing. Try importing application information");
        }
        var webClient = WebClient.create();
        return webClient.get()
            .uri(appUrl.get())
            .retrieve()
            .onStatus(HttpStatus::is4xxClientError,
                response -> Mono.error(new ApplicationHealthEndpointUnreachableException(
                    "client responded with" + response.statusCode().getReasonPhrase())))
            .bodyToMono(HealthCheckResponse.class).onErrorResume(e -> {
                log.warn("Applicaiton with id:{} not available ", appId);
                return Mono.just(new HealthCheckResponse("DOWN"));
            });
    }
}
