package com.fine_kuo.spring_data_jpa.dto.response;

import com.fine_kuo.spring_data_jpa.dto.AllowedSkillLevelMappingDTO;

import java.util.List;

public record GetAllowedSkillLevelPayload(List<AllowedSkillLevelMappingDTO> allowedSkillLevelMappings) {

}
