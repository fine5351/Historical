package com.pkuo.spring_data_jpa.entity;

import com.pkuo.spring_data_jpa.constant.ResumeStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Resume extends com.pkuo.spring_data_jpa.entity.BaseEntity {

    private String fileName;
    @Enumerated(EnumType.ORDINAL)
    private ResumeStatus status;

}
