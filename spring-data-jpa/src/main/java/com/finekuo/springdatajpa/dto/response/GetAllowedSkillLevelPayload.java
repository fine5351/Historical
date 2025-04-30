package com.finekuo.springdatajpa.dto.response;

import com.finekuo.springdatajpa.dto.AllowedSkillLevelMappingDTO;

import java.util.List;

public record GetAllowedSkillLevelPayload(List<AllowedSkillLevelMappingDTO> allowedSkillLevelMappings) {

}
