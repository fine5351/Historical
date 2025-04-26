package com.pkuo.springdatajpa.dto.response;

import com.pkuo.springdatajpa.dto.SkillDTO;

import java.util.List;

public record GetSkillPayload(List<SkillDTO> skills) {

}
