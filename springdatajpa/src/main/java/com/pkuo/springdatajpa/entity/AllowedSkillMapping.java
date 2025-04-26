package com.jkos.hackathon.entity;

import com.jkos.hackathon.constant.SkillLevel;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class AllowedSkillMapping extends com.jkos.hackathon.entity.BaseEntity {

    private long teamId;
    private long vacancyId;
    private long skillId;
    @Enumerated(EnumType.ORDINAL)
    private SkillLevel skillLevel;

}
