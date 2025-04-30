package com.finekuo.springdatajpa.dto.response;

import com.finekuo.springdatajpa.dto.SkillDTO;

import java.util.List;

public record GetSkillPayload(List<SkillDTO> skills) {

}
