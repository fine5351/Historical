package com.finekuo.mybatisflexcore.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BaseEntity {

    @Id(keyType = KeyType.Auto)
    private Long id;

    @Column("created_by")
    private String createdBy;
    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_by")
    private String updatedBy;
    @Column("updated_at")
    private LocalDateTime updatedAt;

}
