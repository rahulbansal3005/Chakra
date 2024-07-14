package com.increff.chakra.helper;

//import com.increff.account.client.SecurityUtil;
//import com.increff.account.client.UserPrincipal;

import com.increff.account.client.SecurityUtil;
import com.increff.account.client.UserPrincipal;
import com.increff.chakra.model.common.JobCommon;
import com.increff.chakra.model.constants.JobDelayTypeKeys;
import com.increff.chakra.model.constants.JobParamKeys;
import com.increff.chakra.model.data.JobData;
import com.increff.chakra.pojo.JobMetaDataPojo;
import com.increff.chakra.pojo.JobParamsPojo;
import com.increff.chakra.pojo.JobPojo;
import com.nextscm.commons.spring.common.ApiException;
import com.nextscm.commons.spring.common.ApiStatus;
import com.nextscm.commons.spring.common.ConvertUtil;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class Helper {

    public static JobData convertToJobData(JobPojo jobPojo, List<JobParamsPojo> jobParamsPojoList, List<JobMetaDataPojo> jobMetaDataPojoList) {
        JobData jobData = ConvertUtil.convert(jobPojo, JobData.class);
        jobData.setOrgId(jobPojo.getOrgId());
        Map<JobParamKeys, String> paramKeyMap = jobParamsPojoList.stream().collect(Collectors.toMap(JobParamsPojo::getParamKey, JobParamsPojo::getParamValue));
        jobData.setDelayType(JobDelayTypeKeys.valueOf(paramKeyMap.get(JobParamKeys.DELAY_TYPE)));
        if(jobData.getDelayType().equals(JobDelayTypeKeys.FIXED_TIME))
            jobData.setTime(ZonedDateTime.parse(paramKeyMap.get(JobParamKeys.FIXED_TIME)));
        else
            jobData.setFrequency(Integer.parseInt(paramKeyMap.get(JobParamKeys.DELAY_TIME)));
        jobData.setJobMetaData(jobMetaDataPojoList.stream().collect(Collectors.toMap(JobMetaDataPojo::getParamKey, JobMetaDataPojo::getParamValue)));

        return jobData;
    }

    public static JobPojo convertToJobPojo(JobCommon common, int orgId, Long defaultTimeOut) {
        JobPojo pojo = ConvertUtil.convert(common, JobPojo.class);
        if(Objects.isNull(pojo.getTimeOut()))
            pojo.setTimeOut(defaultTimeOut);
        pojo.setOrgId(orgId);
        return pojo;
    }

    public static List<JobParamsPojo> convertToJobParamsPojo(JobCommon common, Long jobId) {
        List<JobParamsPojo> jobParamsPojoList = new ArrayList<>();
        JobParamsPojo pojo1 = new JobParamsPojo();
        pojo1.setJobId(jobId);
        pojo1.setParamKey(JobParamKeys.DELAY_TYPE);
        pojo1.setParamValue(common.getDelayType().toString());

        JobParamsPojo pojo2 = new JobParamsPojo();
        pojo2.setJobId(jobId);
        if(common.getDelayType().equals(JobDelayTypeKeys.FIXED_TIME)) {
            pojo2.setParamKey(JobParamKeys.FIXED_TIME);
            pojo2.setParamValue(common.getTime().toString());
        } else {
            pojo2.setParamKey(JobParamKeys.DELAY_TIME);
            pojo2.setParamValue(String.valueOf(common.getFrequency()));
        }
        jobParamsPojoList.add(pojo1);
        jobParamsPojoList.add(pojo2);
        return jobParamsPojoList;
    }

    public static List<JobMetaDataPojo> convertToJobMetaDataPojo(JobCommon common, Long jobId) {
        List<JobMetaDataPojo> jobMetaDataPojoList = new ArrayList<>();

        common.getJobMetaData().forEach((key, value) -> {
            JobMetaDataPojo pojo = new JobMetaDataPojo();
            pojo.setJobId(jobId);
            pojo.setParamKey(key);
            pojo.setParamValue(value);
            jobMetaDataPojoList.add(pojo);
        });
        return jobMetaDataPojoList;
    }

    public static int getAccountOrgId() throws ApiException {
        UserPrincipal userPrincipal = SecurityUtil.getPrincipal();
        if (!Objects.isNull(userPrincipal))
            return userPrincipal.getDomainId();
        else
            throw new ApiException(ApiStatus.AUTH_ERROR, "Authentication Failed");
    }
}
