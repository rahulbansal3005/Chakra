package com.increff.chakra.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import java.time.ZonedDateTime;

@Getter
@Setter
@MappedSuperclass
public class AbstractVersionPojo {
    @Version
    @Column(name = "version")
    private int version = 0;

    private ZonedDateTime createdAt = ZonedDateTime.now();

    private ZonedDateTime updatedAt = ZonedDateTime.now();

    @PreUpdate
    public void setUpdatedAt() {
        this.updatedAt = ZonedDateTime.now();
    }
}
