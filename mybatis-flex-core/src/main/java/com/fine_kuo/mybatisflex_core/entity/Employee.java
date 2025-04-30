package com.fine_kuo.mybatisflex_core.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Employee extends BaseEntity {

    private String name;

    private String address;

    private String gender;

    private String rocId;

}