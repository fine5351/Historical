package com.finekuo.springdatajpa.entity;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class EntityColumnMaskFlattened extends BaseEntity {

    private String account;
    private String method;
    private String apiUri;
    private String maskSettings;

}
