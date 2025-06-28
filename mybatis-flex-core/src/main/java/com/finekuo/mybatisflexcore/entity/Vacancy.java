package com.finekuo.mybatisflexcore.entity;

import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 職缺實體類別，對應於資料庫中的 vacancy 表格
 */
@Table("vacancy")
@Data
@EqualsAndHashCode(callSuper = true)
public class Vacancy extends BaseEntity {
    private String title;
}
