package com.pkuo.springdatajpa.entity;

import com.pkuo.springdatajpa.constant.SkillLevel;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class AllowedSkillMapping extends BaseEntity {

    private long teamId;
    private long vacancyId;
    private long skillId;
    @Enumerated(EnumType.ORDINAL)
    private SkillLevel skillLevel;

}
