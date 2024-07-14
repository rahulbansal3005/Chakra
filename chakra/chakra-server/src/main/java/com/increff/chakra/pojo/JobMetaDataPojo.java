package com.increff.chakra.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "jobMetaData", indexes = {@Index(name = "job_idx", columnList = "jobId")})
public class JobMetaDataPojo extends AbstractVersionPojo{

    @Id
    @TableGenerator(name = "job_meta_data_pojo", pkColumnValue = "job_meta_data_pojo", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "job_meta_data_pojo")
    private Long id;

    @Column(nullable = false)
    private String paramKey;

    @Column(nullable = false)
    private String paramValue;

    @Column(nullable = false)
    private Long jobId;
}
