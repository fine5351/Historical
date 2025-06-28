package com.finekuo.mybatisflexcore.entity;

import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 實體欄位遮罩結構實體類別，對應於資料庫中的 entity_column_mask_structure 表格
 */
@Table("entity_column_mask_structure")
@Data
@EqualsAndHashCode(callSuper = true)
public class EntityColumnMaskStructure extends BaseEntity {
    private String method;
    private String account;
    private String apiUri;
    private String maskSettings;
}
