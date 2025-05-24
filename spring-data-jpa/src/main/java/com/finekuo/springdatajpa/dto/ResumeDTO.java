package com.finekuo.springdatajpa.dto;

import java.time.LocalDateTime; // Added import
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResumeDTO {

    private String fileName;
    private Integer status;
    private Long id; // Added id
    private LocalDateTime createdAt; // Added createdAt
    private LocalDateTime updatedAt; // Added updatedAt

    public static ResumeDTO fromEntity(com.finekuo.springdatajpa.entity.Resume resume) {
        if (resume == null) {
            return null;
        }
        return new ResumeDTO(
                resume.getFileName(),
                resume.getStatus() != null ? resume.getStatus().ordinal() : null,
                resume.getId(),
                resume.getCreatedAt(),
                resume.getUpdatedAt()
        );
    }
}
