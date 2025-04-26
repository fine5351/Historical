package com.jkos.mybatisflex.entity;

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

}