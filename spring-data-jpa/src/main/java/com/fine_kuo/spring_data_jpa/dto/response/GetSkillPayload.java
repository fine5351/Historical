package com.fine_kuo.spring_data_jpa.dto.response;

import com.fine_kuo.spring_data_jpa.dto.SkillDTO;

import java.util.List;

public record GetSkillPayload(List<SkillDTO> skills) {

}
