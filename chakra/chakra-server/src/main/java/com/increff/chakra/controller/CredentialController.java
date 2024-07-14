package com.increff.chakra.controller;

import com.increff.chakra.dto.CredentialDto;
import com.nextscm.commons.spring.common.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(path = "/credential")
public class CredentialController {

    @Autowired
    private CredentialDto dto;

    @RequestMapping(method = RequestMethod.POST)
    public void addOrUpdateCredentials(@RequestBody Map<String, String> credentialMap) throws ApiException {
        dto.addOrUpdateCredentials(credentialMap);
    }
}
