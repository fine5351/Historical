package com.pkuo.springdatajpa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for the entity data fetched from the pkuo API
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PkuoEntityDTO {

    private Long id;
    private String name;
    private String description;
    // Add more fields as needed based on the actual API response
}