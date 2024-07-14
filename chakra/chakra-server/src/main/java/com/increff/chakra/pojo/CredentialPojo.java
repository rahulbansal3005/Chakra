package com.increff.chakra.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "credentials", indexes = {@Index(name = "org_idx", columnList = "orgId")})
public class CredentialPojo extends AbstractVersionPojo{

    @Id
    @TableGenerator(name = "credential_pojo", pkColumnValue = "credential_pojo", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "credential_pojo")
    private Long id;
    @Column(nullable = false)
    private String headerKey;
    @Column(nullable = false)
    private String headerValue;
    @Column(nullable = false)
    private int orgId;
}
