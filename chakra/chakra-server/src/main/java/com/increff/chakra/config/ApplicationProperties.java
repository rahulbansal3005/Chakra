package com.increff.chakra.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Setter
@Getter
public class ApplicationProperties {

    @Value("${default.job.timeout:300}")
    private Long defaultJobTimeout;
}
