package com.finekuo.springdatajpa.entity;

import com.finekuo.springdatajpa.convert.RocIdConvert;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Employee extends BaseEntity {

    private String name;

    private String address;

    private String gender;

    @Convert(converter = RocIdConvert.class)
    private String rocId;

}
