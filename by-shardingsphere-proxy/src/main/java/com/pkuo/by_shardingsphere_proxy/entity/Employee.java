package com.pkuo.by_shardingsphere_proxy.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Table("employee")
public class Employee {

    @Id(keyType = KeyType.Auto)
    private Long id;

    private String name;

    private String address;

    private String gender;

    private String rocId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // Schema: nvarchar(50) not null
    @Column("created_by")
    private String createdBy;

    // Schema: nvarchar(50) not null
    @Column("updated_by")
    private String updatedBy;

}