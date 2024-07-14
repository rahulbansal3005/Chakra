package com.increff.chakra.dto;

import com.increff.chakra.api.CredentialApi;
import com.increff.chakra.api.JobApi;
import com.increff.chakra.api.JobLedgerApi;
import com.increff.chakra.model.data.JobData;
import com.increff.chakra.model.data.JobLedgerData;
import com.increff.chakra.model.response.JobLedgerEventResponse;
import com.increff.chakra.pojo.*;
import com.nextscm.commons.spring.common.ApiException;
import com.nextscm.commons.spring.server.AbstractDtoApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.increff.chakra.helper.Helper.convertToJobData;

@Service
public class JobLedgerDto extends AbstractDtoApi {

    @Autowired
    private JobLedgerApi ledgerApi;

    @Autowired
    private JobApi jobApi;

    @Autowired
    private CredentialApi credentialApi;

    public JobLedgerEventResponse getLedgerEvents(Long mindId) throws ApiException {
        List<JobLedgerPojo> jobLedgerPojos = ledgerApi.getLedgerEvents(mindId);
        List<JobLedgerData> jobLedgerDataList = new ArrayList<>();

        if(!CollectionUtils.isEmpty(jobLedgerPojos)) {
            for (JobLedgerPojo pojo : jobLedgerPojos) {
                JobLedgerData data = new JobLedgerData();
                data.setJobData(getJobDetails(pojo.getJobId()));
                List<CredentialPojo> credentialPojos = credentialApi.getCredentials(data.getJobData().getOrgId());
                Map<String, String> credentialMap = credentialPojos.stream().collect(Collectors.toMap(CredentialPojo::getHeaderKey, CredentialPojo::getHeaderValue));
                data.getJobData().setOrgCredentials(credentialMap);
                data.setEventType(pojo.getEventType());
                data.setId(pojo.getId());
                jobLedgerDataList.add(data);
            }
        }

        JobLedgerEventResponse response = new JobLedgerEventResponse();
        response.setEvents(jobLedgerDataList);
        return response;
    }

    public JobData getJobDetails(Long jobId) throws ApiException {
        JobPojo jobPojo = jobApi.getCheckJob(jobId);
        List<JobParamsPojo> jobParamsPojoList = jobApi.getJobParams(jobId);
        List<JobMetaDataPojo> jobMetaDataPojoList = jobApi.getJobMetaData(jobPojo.getId());
        return convertToJobData(jobPojo, jobParamsPojoList, jobMetaDataPojoList);
    }

}
