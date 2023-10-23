package com.jkos.hackathon.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
public class Skill extends BaseEntity {

    private String name;

}

