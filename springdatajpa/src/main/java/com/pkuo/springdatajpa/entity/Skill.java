package com.jkos.hackathon.entity;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Skill extends com.jkos.hackathon.entity.BaseEntity {

    private String name;

}

