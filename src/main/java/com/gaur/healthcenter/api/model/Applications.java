package com.gaur.healthcenter.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;
import lombok.Builder;
import lombok.Data;

/**
 * @author Harshit Gaur <harshit@gaurs.in>
 */
@Data
@JsonInclude(Include.NON_NULL)
@Builder
public class Applications {

    private List<ExternalAppStatus> externalAppStatus;
}
