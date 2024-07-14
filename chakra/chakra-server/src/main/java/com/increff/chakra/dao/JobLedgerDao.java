package com.increff.chakra.dao;

import com.increff.chakra.pojo.JobLedgerPojo;
import com.nextscm.commons.spring.db.AbstractDao;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class JobLedgerDao extends AbstractDao<JobLedgerPojo> {

    public List<JobLedgerPojo> selectLedgerEvents(Long minId) {
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaQuery<JobLedgerPojo> query = cb.createQuery(JobLedgerPojo.class);
        Root<JobLedgerPojo> root = query.from(JobLedgerPojo.class);
        query.where(cb.greaterThan(root.get("id"), minId));
        return selectMultiple(createQuery(query));
    }
}
