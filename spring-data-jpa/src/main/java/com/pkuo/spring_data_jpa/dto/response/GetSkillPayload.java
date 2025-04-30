package com.pkuo.spring_data_jpa.dto.response;

import com.pkuo.spring_data_jpa.dto.SkillDTO;

import java.util.List;

public record GetSkillPayload(List<SkillDTO> skills) {

}
