package com.increff.chakra.controller;

import com.increff.chakra.dto.JobDto;
import com.increff.chakra.model.form.JobForm;
import com.nextscm.commons.spring.common.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/job")
public class JobController {

    @Autowired
    private JobDto dto;

    @RequestMapping(method = RequestMethod.POST)
    public void createJob(@RequestBody JobForm form) throws ApiException {
        dto.addJob(form);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public void updateJob(@RequestBody JobForm form) throws ApiException {
        dto.updateJob(form);
    }


}
