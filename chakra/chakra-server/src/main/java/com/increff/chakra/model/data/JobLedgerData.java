package com.increff.chakra.model.data;

import com.increff.chakra.model.constants.JobEventType;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class JobLedgerData{

    @NotNull
    private Long id;
    @NotNull
    private JobEventType eventType;
    @NotNull
    private JobData jobData;
}
