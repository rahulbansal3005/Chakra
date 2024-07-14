package com.increff.chakra.controller;

import com.increff.chakra.dto.JobLedgerDto;
import com.increff.chakra.model.response.JobLedgerEventResponse;
import com.nextscm.commons.spring.common.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/job-ledger")
public class JobLedgerController {

    @Autowired
    private JobLedgerDto dto;

    @RequestMapping(method = RequestMethod.GET)
    public JobLedgerEventResponse getEvents(@RequestParam Long minId) throws ApiException {
        return dto.getLedgerEvents(minId);
    }
}
