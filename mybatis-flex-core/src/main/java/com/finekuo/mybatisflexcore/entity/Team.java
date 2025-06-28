package com.finekuo.mybatisflexcore.entity;

import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 團隊實體類別，對應於資料庫中的 team 表格
 */
@Table("team")
@Data
@EqualsAndHashCode(callSuper = true)
public class Team extends BaseEntity {
    private String name;
}
