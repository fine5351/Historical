package com.jkos.hackathon.entity;

import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class Team extends BaseEntity {

    private String name;

}
