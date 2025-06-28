package com.finekuo.mybatisflexcore.entity;

import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 技能實體類別，對應於資料庫中的 skill 表格
 */
@Table("skill")
@Data
@EqualsAndHashCode(callSuper = true)
public class Skill extends BaseEntity {
    private String name;
}
