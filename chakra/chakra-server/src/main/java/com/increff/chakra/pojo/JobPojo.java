package com.increff.chakra.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "jobs", uniqueConstraints = {@UniqueConstraint(name = "uk_job_org", columnNames = {"orgId", "jobName"})})
public class JobPojo extends AbstractVersionPojo{

    @Id
    @TableGenerator(name = "job_pojo", pkColumnValue = "job_pojo", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "job_pojo")
    private Long id;

    @Column(nullable = false)
    private String jobName;

    @Column(nullable = false)
    private Boolean isEnabled;

    @Column(nullable = false)
    private Long timeOut;

    @Column(nullable = true)
    private int orgId;

    @Column(nullable = false)
    private String url;
}
