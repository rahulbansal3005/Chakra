package com.increff.chakra.dto;

import com.increff.chakra.api.CredentialApi;
import com.increff.chakra.pojo.CredentialPojo;
import com.nextscm.commons.spring.common.ApiException;
import com.nextscm.commons.spring.common.ApiStatus;
import com.nextscm.commons.spring.server.AbstractDtoApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.increff.chakra.helper.Helper.getAccountOrgId;

@Service
public class CredentialDto extends AbstractDtoApi {

    @Autowired
    private CredentialApi credentialApi;

    public void addOrUpdateCredentials(Map<String, String> credentials) throws ApiException {
        if(credentials.keySet().size() == 0)
            throw new ApiException(ApiStatus.BAD_DATA, "No credentials provided");

        List<CredentialPojo> credentialPojoList = credentialApi.getCredentials(getAccountOrgId());
        if(credentialPojoList.size() > 0)
            credentialApi.deleteCredentials(credentialPojoList.stream().map(CredentialPojo::getId).collect(Collectors.toList()));

        credentialApi.saveCredentials(credentials, getAccountOrgId());
    }
}
