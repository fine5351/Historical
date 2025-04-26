package com.pkuo.springdatajpa.dto.response;

import com.pkuo.springdatajpa.dto.AllowedSkillLevelMappingDTO;

import java.util.List;

public record GetAllowedSkillLevelPayload(List<AllowedSkillLevelMappingDTO> allowedSkillLevelMappings) {

}
