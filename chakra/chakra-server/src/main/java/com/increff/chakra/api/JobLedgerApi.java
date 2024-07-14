package com.increff.chakra.api;

import com.increff.chakra.dao.JobLedgerDao;
import com.increff.chakra.model.constants.JobEventType;
import com.increff.chakra.pojo.JobLedgerPojo;
import com.nextscm.commons.spring.server.AbstractApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class JobLedgerApi extends AbstractApi {

    @Autowired
    private JobLedgerDao dao;

    public void saveEvent(Long jobId, JobEventType eventType) {
        JobLedgerPojo pojo = new JobLedgerPojo();
        pojo.setJobId(jobId);
        pojo.setEventType(eventType);
        dao.persist(pojo);
    }

    public List<JobLedgerPojo> getLedgerEvents(Long minId) {
        return dao.selectLedgerEvents(minId);
    }
}
