package com.increff.chakra.model.common;

import com.increff.chakra.model.constants.JobDelayTypeKeys;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.Map;

@Getter
@Setter
public class JobCommon {

    @NotNull
    private String jobName;
    @NotNull
    private String url;
    @NotNull
    private Boolean isEnabled;
    @NotNull
    private JobDelayTypeKeys delayType;

    private Integer frequency;

    private ZonedDateTime time;

    private Long timeOut;

    private Map<String, String> jobMetaData;

    private Map<String, String> orgCredentials;
}
