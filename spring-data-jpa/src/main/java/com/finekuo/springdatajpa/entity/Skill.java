package com.finekuo.springdatajpa.entity;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Skill extends BaseEntity {

    private String name;

}

