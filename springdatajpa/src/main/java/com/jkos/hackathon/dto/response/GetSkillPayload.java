package com.jkos.hackathon.dto.response;

import com.jkos.hackathon.dto.SkillDTO;

import java.util.List;

public record GetSkillPayload(List<SkillDTO> skills) {

}
