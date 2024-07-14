package com.increff.chakra.api;

import com.increff.chakra.config.ApplicationProperties;
import com.increff.chakra.dao.JobDao;
import com.increff.chakra.dao.JobMetaDataDao;
import com.increff.chakra.dao.JobParamsDao;
import com.increff.chakra.model.common.JobCommon;
import com.increff.chakra.model.constants.JobDelayTypeKeys;
import com.increff.chakra.model.constants.JobParamKeys;
import com.increff.chakra.pojo.JobMetaDataPojo;
import com.increff.chakra.pojo.JobParamsPojo;
import com.increff.chakra.pojo.JobPojo;
import com.nextscm.commons.spring.common.ApiException;
import com.nextscm.commons.spring.common.ApiStatus;
import com.nextscm.commons.spring.server.AbstractApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static com.increff.chakra.helper.Helper.*;

@Service
@Transactional
public class JobApi extends AbstractApi {

    @Autowired
    private JobDao jobDao;
    @Autowired
    private JobParamsDao jobParamsDao;
    @Autowired
    private JobMetaDataDao jobMetaDataDao;
    @Autowired
    private ApplicationProperties applicationProperties;

    public Long saveJobDetails(JobCommon jobCommon, int orgId) throws ApiException {
        if(Objects.nonNull(jobDao.selectJobByNameAndOrg(jobCommon.getJobName(), orgId)))
            throw new ApiException(ApiStatus.RESOURCE_EXISTS, "Job: " + jobCommon.getJobName() + " already exists");

        JobPojo jobPojo = convertToJobPojo(jobCommon, orgId, applicationProperties.getDefaultJobTimeout());
        jobDao.persist(jobPojo);

        convertToJobParamsPojo(jobCommon, jobPojo.getId()).forEach(p->{jobParamsDao.persist(p);});

        List<JobMetaDataPojo> metaDataPojoList = convertToJobMetaDataPojo(jobCommon, jobPojo.getId());
        if(metaDataPojoList.size() > 0) {
            metaDataPojoList.forEach(p-> {jobMetaDataDao.persist(p);});
        }
        return jobPojo.getId();
    }

    public void updateJobDetails(JobCommon jobcommon, Long jobId) {
        JobPojo pojo = jobDao.select(jobId);
        pojo.setIsEnabled(jobcommon.getIsEnabled());
        pojo.setUrl(jobcommon.getUrl());
        if(Objects.nonNull(jobcommon.getTimeOut()))
            pojo.setTimeOut(jobcommon.getTimeOut());

        List<JobParamsPojo> jobParamsPojoList = jobParamsDao.selectMultiple("jobId", jobId);
        JobParamsPojo delayTypePojo = jobParamsPojoList.stream().filter(p->p.getParamKey().equals(JobParamKeys.DELAY_TYPE)).findFirst().get();
        jobParamsPojoList.remove(delayTypePojo);
        if (delayTypePojo.getParamValue().equals(jobcommon.getDelayType().toString())) {
            if(jobcommon.getDelayType().equals(JobDelayTypeKeys.FIXED_TIME)) {
                jobParamsPojoList.get(0).setParamValue(jobcommon.getTime().toString());
            } else {
                jobParamsPojoList.get(0).setParamValue(String.valueOf(jobcommon.getFrequency()));
            }
        }
        else {
            delayTypePojo.setParamValue(jobcommon.getDelayType().toString());
            if (jobcommon.getDelayType().equals(JobDelayTypeKeys.FIXED_TIME)) {
                jobParamsPojoList.get(0).setParamKey(JobParamKeys.FIXED_TIME);
                jobParamsPojoList.get(0).setParamValue(jobcommon.getTime().toString());
            } else {
                jobParamsPojoList.get(0).setParamKey(JobParamKeys.DELAY_TIME);
                jobParamsPojoList.get(0).setParamValue(String.valueOf(jobcommon.getFrequency()));
            }
        }

        jobMetaDataDao.selectMultiple("jobId", jobId).forEach(p->{
            jobMetaDataDao.remove(p);
        });

        List<JobMetaDataPojo> metaDataPojoList = convertToJobMetaDataPojo(jobcommon, jobId);
        if(metaDataPojoList.size() > 0) {
            metaDataPojoList.forEach(p-> {jobMetaDataDao.persist(p);});
        }
    }

    public JobPojo getCheckJob(String jobName, int orgId) throws ApiException {
        JobPojo jobPojo = jobDao.selectJobByNameAndOrg(jobName, orgId);
        checkNotNull(jobPojo, "Job does not exists for Job Name: " + jobName);
        return jobPojo;
    }

    public List<JobParamsPojo> getJobParams(Long jobId) {
        return jobParamsDao.selectMultiple("jobId",jobId);
    }

    public List<JobMetaDataPojo> getJobMetaData(Long jobId) {
        return jobMetaDataDao.selectMultiple("jobId", jobId);
    }

    public JobPojo getCheckJob(Long jobId) throws ApiException {
        JobPojo jobPojo = jobDao.select(jobId);
        checkNotNull(jobPojo, "Job does not exists for Job Id: " + jobId);
        return jobPojo;
    }

}
