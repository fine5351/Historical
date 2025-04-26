package com.jkos.hackathon.entity;

import com.jkos.hackathon.constant.ResumeStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Resume extends com.jkos.hackathon.entity.BaseEntity {

    private String fileName;
    @Enumerated(EnumType.ORDINAL)
    private ResumeStatus status;

}
