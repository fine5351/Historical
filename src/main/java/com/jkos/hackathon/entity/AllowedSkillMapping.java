package com.jkos.hackathon.entity;

import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class AllowedSkillMapping extends BaseEntity {

    private long teamId;
    private long skillId;
    private int level;

}
