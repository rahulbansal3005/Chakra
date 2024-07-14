package com.increff.chakra.dao;

import com.increff.chakra.pojo.JobPojo;
import com.nextscm.commons.spring.db.AbstractDao;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Repository
public class JobDao extends AbstractDao<JobPojo> {

    public JobPojo selectJobByNameAndOrg(String jobName, int orgId) {
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaQuery<JobPojo> query = cb.createQuery(JobPojo.class);
        Root<JobPojo> root = query.from(JobPojo.class);
        query.where(cb.and(cb.equal(root.get("jobName"), jobName),
                cb.equal(root.get("orgId"), orgId)));
        return selectSingleOrNull(createQuery(query));
    }
}
