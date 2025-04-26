package com.pkuo.springdatajpa.entity;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Vacancy extends com.pkuo.springdatajpa.entity.BaseEntity {

    private String title;

}
