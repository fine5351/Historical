package com.finekuo.mybatisflexcore.entity;

import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 員工實體類別，對應於資料庫中的 employee 表格
 */
@Table("employee")
@EqualsAndHashCode(callSuper = true)
@Data
public class Employee extends BaseEntity {

    private String name;

    private String address;

    private String gender;

    private String rocId;

}
