package com.increff.chakra.pojo;

import com.increff.chakra.model.constants.JobParamKeys;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "jobParams", indexes = {@Index(name = "job_idx", columnList = "jobId")})
public class JobParamsPojo extends AbstractVersionPojo {

    @Id
    @TableGenerator(name = "job_params_pojo", pkColumnValue = "job_params_pojo", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "job_params_pojo")
    private Long id;

    @Column(nullable = false)
    private JobParamKeys paramKey;

    @Column(nullable = false)
    private String paramValue;

    @Column(nullable = false)
    private Long jobId;

}
