package com.finekuo.mybatisflexcore.entity;

import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 允許技能映射實體類別，對應於資料庫中的 allowed_skill_mapping 表格
 */
@Table("allowed_skill_mapping")
@Data
@EqualsAndHashCode(callSuper = true)
public class AllowedSkillMapping extends BaseEntity {
    private Long teamId;
    private Long vacancyId;
    private Long skillId;
    private Integer skillLevel;
}
