package com.jkos.hackathon.dto.response;

import com.jkos.hackathon.dto.AllowedSkillLevelMappingDTO;

import java.util.List;

public record GetAllowedSkillLevelPayload(List<AllowedSkillLevelMappingDTO> allowedSkillLevelMappings) {

}
