package com.pkuo.spring_data_jpa.dto.response;

import com.pkuo.spring_data_jpa.dto.AllowedSkillLevelMappingDTO;

import java.util.List;

public record GetAllowedSkillLevelPayload(List<AllowedSkillLevelMappingDTO> allowedSkillLevelMappings) {

}
