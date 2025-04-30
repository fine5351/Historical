package com.fine_kuo.spring_data_jpa.entity;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Team extends BaseEntity {

    private String name;

}
