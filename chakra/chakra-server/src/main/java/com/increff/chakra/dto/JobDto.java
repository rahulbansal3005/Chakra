package com.increff.chakra.dto;

import com.increff.chakra.api.CredentialApi;
import com.increff.chakra.api.JobApi;
import com.increff.chakra.api.JobLedgerApi;
import com.increff.chakra.model.constants.JobDelayTypeKeys;
import com.increff.chakra.model.constants.JobEventType;
import com.increff.chakra.model.constants.JobParamKeys;
import com.increff.chakra.model.form.JobForm;
import com.increff.chakra.pojo.CredentialPojo;
import com.increff.chakra.pojo.JobParamsPojo;
import com.increff.chakra.pojo.JobPojo;
import com.nextscm.commons.spring.common.ApiException;
import com.nextscm.commons.spring.common.ApiStatus;
import com.nextscm.commons.spring.server.AbstractDtoApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static com.increff.chakra.helper.Helper.getAccountOrgId;

@Service
public class JobDto extends AbstractDtoApi {

    @Autowired
    private JobApi jobApi;

    @Autowired
    private JobLedgerApi jobLedgerApi;

    @Autowired
    private CredentialApi credentialApi;

    @Transactional(rollbackFor = ApiException.class)
    public void addJob(JobForm form) throws ApiException {
        validateJobForm(form);
        normalizeJobName(form);
        form.setJobName(form.getJobName().toUpperCase());
        List<CredentialPojo> credentialPojos = credentialApi.getCredentials(getAccountOrgId());
        if(credentialPojos.size() == 0 )
            throw new ApiException(ApiStatus.BAD_DATA, "Please first add credentials for this org");
        Long jobId = jobApi.saveJobDetails(form, getAccountOrgId());
        jobLedgerApi.saveEvent(jobId, JobEventType.CREATE);
    }

    @Transactional(rollbackFor = ApiException.class)
    public void updateJob(JobForm form) throws ApiException {
        validateJobForm(form);
        normalizeJobName(form);
        JobPojo jobPojo = jobApi.getCheckJob(form.getJobName(), getAccountOrgId());
        List<JobParamsPojo> jobParamsPojos = jobApi.getJobParams(jobPojo.getId());
        JobParamsPojo delayType = jobParamsPojos.stream().filter(p->p.getParamKey().equals(JobParamKeys.DELAY_TYPE)).findFirst().get();

        if(!Objects.equals(delayType.getParamValue(), form.getDelayType().toString()))
            throw new ApiException(ApiStatus.BAD_DATA, "Cannot update DelayType from " + delayType.getParamValue() + " to "
                                    + form.getDelayType());
        jobApi.updateJobDetails(form, jobPojo.getId());
        jobLedgerApi.saveEvent(jobPojo.getId(), JobEventType.UPDATE);
    }

    private void validateJobForm(JobForm form) throws ApiException {
        checkValid(form);
        if((form.getDelayType().equals(JobDelayTypeKeys.FIXED_DELAY) || form.getDelayType().equals(JobDelayTypeKeys.FIXED_RATE))
                && Objects.isNull(form.getFrequency()))
            throw new ApiException(ApiStatus.BAD_DATA, "Frequency should be provided for FIXED_DELAY and FIXED_RATE Jobs");

        if(form.getDelayType().equals(JobDelayTypeKeys.FIXED_TIME) && Objects.isNull(form.getTime()))
            throw new ApiException(ApiStatus.BAD_DATA, "Time should be provided for FIXED_TIME delay Jobs");
    }

    private void normalizeJobName(JobForm form) {
        form.setJobName(form.getJobName().toUpperCase());
    }
}
