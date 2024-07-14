package com.increff.chakra.api;

import com.increff.chakra.dao.CredentialDao;
import com.increff.chakra.pojo.CredentialPojo;
import com.nextscm.commons.spring.server.AbstractApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class CredentialApi extends AbstractApi {

    @Autowired
    private CredentialDao dao;

    public void saveCredentials(Map<String, String> credentialMap, Integer orgId) {
        for( String key : credentialMap.keySet()) {
            CredentialPojo credentialPojo = new CredentialPojo();
            credentialPojo.setHeaderKey(key);
            credentialPojo.setHeaderValue(credentialMap.get(key));
            credentialPojo.setOrgId(orgId);
            dao.persist(credentialPojo);
        }
    }

    public List<CredentialPojo> getCredentials(List<Integer> orgIds) {
        return dao.selectCredentialsForOrgIds(orgIds);
    }

    public List<CredentialPojo> getCredentials(Integer orgId) {
        return dao.selectMultiple("orgId", orgId);
    }

    public void deleteCredentials(List<Long> credentialIds) {
        for(Long id : credentialIds) {
            dao.remove(id);
        }
    }
}
