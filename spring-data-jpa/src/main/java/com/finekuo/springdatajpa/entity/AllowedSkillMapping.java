package com.finekuo.springdatajpa.entity;

import com.finekuo.normalcore.constant.SkillLevel;
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
