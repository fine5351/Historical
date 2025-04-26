package com.pkuo.springdatajpa.entity;

import com.pkuo.springdatajpa.constant.ResumeStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Resume extends BaseEntity {

    private String fileName;
    @Enumerated(EnumType.ORDINAL)
    private ResumeStatus status;

}
