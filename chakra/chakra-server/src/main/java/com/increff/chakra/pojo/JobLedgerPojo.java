package com.increff.chakra.pojo;

import com.increff.chakra.model.constants.JobEventType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "jobLedger", indexes = {@Index(name = "job_idx", columnList = "jobId")})
public class JobLedgerPojo extends AbstractVersionPojo{

    @Id
    @TableGenerator(name = "jon_ledger_pojo", pkColumnValue = "jon_ledger_pojo", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "jon_ledger_pojo")
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private JobEventType eventType;

    @Column
    private Long jobId;
}
