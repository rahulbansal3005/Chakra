package com.increff.chakra;

import com.increff.chakra.model.form.JobForm;
import com.nextscm.commons.spring.client.AbstractAppClient;
import com.nextscm.commons.spring.client.AppClientException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class ChakraClient extends AbstractAppClient{

    private static final String JOB = "/job";
    private static final String CREDENTIAL = "/credential";

    public ChakraClient(String baseUrl) {
        super(baseUrl);
    }

    public ChakraClient(String baseUrl, RestTemplate restTemplate) {
        super(baseUrl, restTemplate);
    }

    public void setCredentials(String authUserName, String authPassword, String authDomainName) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("authUsername", authUserName);
        headers.add("authPassword", authPassword);
        headers.add("authDomainName", authDomainName);
        this.setBaseHeaders(headers);
    }

    public void addJob(JobForm form) throws AppClientException {
        makeRequest(HttpMethod.POST, JOB, getHeaders(), null, form, Void.class);
    }

    public void updateJob(JobForm form) throws AppClientException {
        makeRequest(HttpMethod.PUT, JOB, getHeaders(), null, form, Void.class);
    }

    public void addOrUpdateCredentials(Map<String, String> credentials) throws AppClientException {
        makeRequest(HttpMethod.POST, CREDENTIAL, getHeaders(), null, credentials, Void.class);
    }

}
