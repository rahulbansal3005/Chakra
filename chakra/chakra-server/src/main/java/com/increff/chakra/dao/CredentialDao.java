package com.increff.chakra.dao;

import com.increff.chakra.pojo.CredentialPojo;
import com.nextscm.commons.spring.db.AbstractDao;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class CredentialDao extends AbstractDao<CredentialPojo> {

    public List<CredentialPojo> selectCredentialsForOrgIds(List<Integer> orgIds) {
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaQuery<CredentialPojo> query = cb.createQuery(CredentialPojo.class);
        Root<CredentialPojo> root = query.from(CredentialPojo.class);
        query.where(root.get("orgId").in(orgIds));
        return selectMultiple(createQuery(query));
    }
}
