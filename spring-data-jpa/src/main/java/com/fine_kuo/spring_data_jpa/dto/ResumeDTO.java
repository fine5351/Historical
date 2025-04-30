package com.fine_kuo.spring_data_jpa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResumeDTO {

    private String fileName;
    private Integer status;

}
