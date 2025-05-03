package com.finekuo.springdatajpa.entity;

import com.finekuo.normalcore.constant.ResumeStatus;
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
