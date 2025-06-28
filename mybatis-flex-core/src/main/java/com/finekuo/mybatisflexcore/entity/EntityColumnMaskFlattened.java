package com.finekuo.mybatisflexcore.entity;

import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 實體欄位遮罩扁平化實體類別，對應於資料庫中的 entity_column_mask_flattened 表格
 */
@Table("entity_column_mask_flattened")
@Data
@EqualsAndHashCode(callSuper = true)
public class EntityColumnMaskFlattened extends BaseEntity {
    private String method;
    private String account;
    private String apiUri;
    private String maskSettings;
}
