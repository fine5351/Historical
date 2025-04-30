package com.pkuo.spring_data_jpa.entity;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Skill extends com.pkuo.spring_data_jpa.entity.BaseEntity {

    private String name;

}

