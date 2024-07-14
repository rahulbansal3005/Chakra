package com.increff.chakra.model.response;

import com.increff.chakra.model.data.JobLedgerData;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JobLedgerEventResponse {

    List<JobLedgerData> events;
}
