package com.finekuo.mybatisflexcore.entity;

import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 履歷實體類別，對應於資料庫中的 resume 表格
 */
@Table("resume")
@Data
@EqualsAndHashCode(callSuper = true)
public class Resume extends BaseEntity {
    private String fileName;
    private Integer status;
}
